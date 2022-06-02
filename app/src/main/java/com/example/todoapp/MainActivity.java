package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements AddNewTaskDialog.TaskDialogListener {

    private ArrayList<TaskItem> taskItems = new ArrayList<>();
    private ToDoListDB toDoListDB = new ToDoListDB(this);
    private TaskRecycleViewAdapter taskRecycleViewAdapter;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskItems = toDoListDB.getTaskList();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        taskRecycleViewAdapter = new TaskRecycleViewAdapter(taskItems, this);
        recyclerView.setAdapter(taskRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void openDialog() {
        AddNewTaskDialog addNewTaskDialog = new AddNewTaskDialog(this);
        addNewTaskDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String taskTitle, String taskDescription, String taskDeadLineDate) {
        addNewTask(taskTitle, taskDescription, taskDeadLineDate);
    }

    private void addNewTask(String taskTitle, String taskDescription, String taskDeadLineDate) {
        TaskItem newItem = new TaskItem(taskItems.size(), taskTitle, taskDescription, LocalDate.now().toString(), "", taskDeadLineDate);
        taskItems.add(newItem);
        int id = toDoListDB.insertIntoTheDatabase(newItem);
        newItem.setKey_id(id);
        taskRecycleViewAdapter.notifyItemInserted(taskItems.size()-1);
    }

    TaskItem deletedTaskItem;

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT /*| ItemTouchHelper.RIGHT*/) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_forever_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction)
            {
                case ItemTouchHelper.LEFT:
                    toDoListDB.removeFromTheDatabase(taskItems.get(position).getKey_id());
                    deletedTaskItem = taskItems.get(position);
                    taskItems.remove(position);
                    taskRecycleViewAdapter.notifyItemRemoved(position);
                    Snackbar.make(findViewById(R.id.recyclerView),deletedTaskItem.getTitle(),Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            taskItems.add(position,deletedTaskItem);
                            int id = toDoListDB.insertIntoTheDatabase(deletedTaskItem);
                            deletedTaskItem.setKey_id(id);
                            taskRecycleViewAdapter.notifyItemInserted(position);
                        }
                    }).show();

            }


        }

    };
}