package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TaskItem> taskItems = new ArrayList<>();
    private ToDoListDB toDoListDB = new ToDoListDB(this);
    private TaskRecycleViewAdapter taskRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        taskRecycleViewAdapter = new TaskRecycleViewAdapter(taskItems, this);
        recyclerView.setAdapter(taskRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}