package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskDetailsActivity extends AppCompatActivity {

    EditText editTextTitle,editTextDeadLineDate,editTextCreateDate,editTextFinishDateDate,editTextDescription;
    Button doneButton;
    TaskItem taskItem;
    private ToDoListDB toDoListDB = new ToDoListDB(this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskItem = getIntent().getParcelableExtra("POSITION");

        editTextTitle = findViewById(R.id.titleEditText);
        editTextDeadLineDate = findViewById(R.id.deadLineEditText);
        editTextCreateDate = findViewById(R.id.createDateEditText);
        editTextFinishDateDate = findViewById(R.id.finishDateEditText);
        editTextDescription = findViewById(R.id.descriptionEditText);
        doneButton = findViewById(R.id.doneButton);
        doneButtonHandle();

        editTextTitle.setText(taskItem.getTitle());
        editTextDeadLineDate.setText(taskItem.getDeadLineDate()+" "+taskItem.getDeadLineTime());
        editTextCreateDate.setText(taskItem.getCreateDate()+" "+taskItem.getCreateTime());
        editTextFinishDateDate.setText(taskItem.getFinishDate()+" "+taskItem.getFinishTime());
        editTextDescription.setText(taskItem.getDescription());
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskItem.getFinishDate().equals("")) {
                    taskItem.setFinishDate(LocalDate.now().toString());
                    taskItem.setFinishTime(LocalTime.now().getHour()+":"+LocalTime.now().getMinute());
                }
                else
                {
                    taskItem.setFinishDate("");
                    taskItem.setFinishTime("");
                }

                doneButtonHandle();
                toDoListDB.updateTask(taskItem);
            }
        });
    }

    void doneButtonHandle()
    {
        if(taskItem.getFinishDate().equals("")) {
            editTextTitle.setPaintFlags(editTextTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            editTextTitle.setTextColor(Color.parseColor("#000000"));
            doneButton.setBackgroundResource(R.drawable.ic_baseline_panorama_fish_eye_24);
        }
        else {
            editTextTitle.setPaintFlags(editTextTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            editTextTitle.setTextColor(Color.parseColor("#676767"));
            doneButton.setBackgroundResource(R.drawable.ic_baseline_check_circle_24);
        }
        editTextFinishDateDate.setText(taskItem.getFinishDate()+" "+taskItem.getFinishTime());
    }

}