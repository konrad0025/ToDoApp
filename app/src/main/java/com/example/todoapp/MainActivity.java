package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;

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

    }

    private void openDialog() {
        AddNewTaskDialog addNewTaskDialog = new AddNewTaskDialog(this);
        addNewTaskDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String taskTitle, String taskDescription, String taskDeadLineDate) {
        addNewTask(taskTitle, taskDescription, taskDeadLineDate);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addNewTask(String taskTitle, String taskDescription, String taskDeadLineDate) {
        TaskItem newItem = new TaskItem(taskItems.size(), taskTitle, taskDescription, LocalDate.now().toString(), "", taskDeadLineDate);
        taskItems.add(newItem);
        int id = toDoListDB.insertIntoTheDatabase(newItem);
        newItem.setKey_id(id);
        taskRecycleViewAdapter.notifyDataSetChanged();
    }
}