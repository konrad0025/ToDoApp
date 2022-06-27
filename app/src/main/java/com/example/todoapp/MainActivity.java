package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements AddNewTaskDialog.TaskDialogListener, Serializable {

    private ArrayList<TaskItem> taskItems = new ArrayList<>();
    private ToDoListDB toDoListDB = new ToDoListDB(this);
    private TaskRecycleViewAdapter taskRecycleViewAdapter;
    private FloatingActionButton addButton;
    private TaskRecycleViewAdapter.RecyclerViewClickListener listener;
    private RecyclerView recyclerView;
    private EditText editTextFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskItems = toDoListDB.getTaskList();

        setOnClickListener();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        taskRecycleViewAdapter = new TaskRecycleViewAdapter(taskItems, this, listener);
        filterList("");
        recyclerView.setAdapter(taskRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        editTextFilter = findViewById(R.id.filterEditText);
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterList(editable.toString());
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void filterList(String text) {
        ArrayList<TaskItem> filteredList = new ArrayList<>();

        for (TaskItem item : taskItems){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        taskRecycleViewAdapter.filterListUpdate(filteredList);
    }

    private void openDialog() {
        AddNewTaskDialog addNewTaskDialog = new AddNewTaskDialog(this);
        addNewTaskDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String taskTitle, String taskDescription, String taskDeadLineDate, String taskDeadLineTime) {
        addNewTask(taskTitle, taskDescription, taskDeadLineDate, taskDeadLineTime);
    }

    private void addNewTask(String taskTitle, String taskDescription, String taskDeadLineDate, String taskDeadLineTime) {
        TaskItem newItem = new TaskItem(-1, taskTitle, taskDescription, LocalDate.now().toString(), LocalTime.now().getHour()+":"+LocalTime.now().getMinute(), "", "", taskDeadLineDate, taskDeadLineTime);
        taskItems.add(newItem);
        int id = toDoListDB.insertIntoTheDatabase(newItem);
        newItem.setKey_id(id);
        taskRecycleViewAdapter.getTaskItems().add(newItem);
        filterList(editTextFilter.getText().toString());
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
                    toDoListDB.removeFromTheDatabase(taskRecycleViewAdapter.getTaskItems().get(position).getKey_id());
                    deletedTaskItem = taskRecycleViewAdapter.getTaskItems().get(position);
                    taskRecycleViewAdapter.getTaskItems().remove(position);
                    taskRecycleViewAdapter.notifyItemRemoved(position);
                    taskItems.remove(taskItems.indexOf(deletedTaskItem));

                    taskItems = toDoListDB.getTaskList();
                    Snackbar.make(findViewById(R.id.recyclerView),deletedTaskItem.getTitle(),Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            taskRecycleViewAdapter.getTaskItems().add(position,deletedTaskItem);
                            toDoListDB.insertIntoTheDatabase(deletedTaskItem);
                            taskRecycleViewAdapter.notifyItemInserted(position);
                            taskItems = toDoListDB.getTaskList();
                        }
                    }).show();
            }
        }
    };

    int onClickTaskItemId;
    int onClickTaskItemPosition;
    private void setOnClickListener() {
        listener = new TaskRecycleViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                intent.putExtra("POSITION", taskRecycleViewAdapter.getTaskItems().get(position));
                onClickTaskItemId = taskRecycleViewAdapter.getTaskItems().get(position).getKey_id();
                onClickTaskItemPosition = position;
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        TaskItem taskItem = toDoListDB.getTaskItem(onClickTaskItemId);
        if(taskItems.contains(taskRecycleViewAdapter.getTaskItems().get(onClickTaskItemPosition)))
        {
            taskItems.set(taskItems.indexOf(taskRecycleViewAdapter.getTaskItems().get(onClickTaskItemPosition)),taskItem);
        }
        taskRecycleViewAdapter.getTaskItems().set(onClickTaskItemPosition,taskItem);
        taskRecycleViewAdapter.notifyItemChanged(onClickTaskItemPosition);
    }
}