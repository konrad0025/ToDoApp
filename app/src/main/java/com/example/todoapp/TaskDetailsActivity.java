package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.todoapp.database.ToDoListDB;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TaskDetailsActivity extends AppCompatActivity {

    EditText editTextTitle,editTextDeadLineDate,editTextCreateDate,editTextFinishDateDate,editTextDescription;
    Button doneButton;
    TaskItem taskItem;
    private ToDoListDB toDoListDB = new ToDoListDB(this);
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    ArrayList<String> categoryList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskItem = getIntent().getParcelableExtra("POSITION");
        categoryList = getIntent().getStringArrayListExtra("LIST_OF_CATEGORY");
        editTextTitle = findViewById(R.id.titleEditText);
        editTextDeadLineDate = findViewById(R.id.deadLineEditText);
        editTextCreateDate = findViewById(R.id.createDateEditText);
        editTextFinishDateDate = findViewById(R.id.finishDateEditText);
        editTextDescription = findViewById(R.id.descriptionEditText);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        doneButton = findViewById(R.id.doneButton);
        doneButtonHandle();

        editTextTitle.setText(taskItem.getTitle());
        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                taskItem.setTitle(editable.toString());
                toDoListDB.updateTask(taskItem);
            }
        });
        editTextDeadLineDate.setText(taskItem.getDeadLineDate()+" "+taskItem.getDeadLineTime());
        editTextDeadLineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate deadlineDate = LocalDate.parse(taskItem.getDeadLineDate());
                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskDetailsActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        taskItem.setDeadLineDate(year + "-" + (month+1<10 ? "0"+(month+1) : (month+1)) + "-" + (day<10 ? "0"+day : day));
                        LocalTime deadLineTime = LocalTime.parse(taskItem.getDeadLineTime());
                        TimePickerDialog timePickerDialog = new TimePickerDialog(TaskDetailsActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                taskItem.setDeadLineTime((hour<10 ? "0"+(hour) : hour)+":"+(min<10 ? "0"+(min) : min));
                                editTextDeadLineDate.setText(taskItem.getDeadLineDate()+" "+taskItem.getDeadLineTime());
                                toDoListDB.updateTask(taskItem);
                            }
                        }, deadLineTime.getHour(),deadLineTime.getMinute(),true);
                        timePickerDialog.show();
                    }
                }, deadlineDate.getYear(), deadlineDate.getMonthValue()-1, deadlineDate.getDayOfMonth());
                datePickerDialog.show();

            }
        });
        editTextCreateDate.setText(taskItem.getCreateDate()+" "+taskItem.getCreateTime());
        editTextFinishDateDate.setText(taskItem.getFinishDate()+" "+taskItem.getFinishTime());
        editTextDescription.setText(taskItem.getDescription());
        editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                taskItem.setDescription(editable.toString());
                toDoListDB.updateTask(taskItem);
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskItem.getFinishDate().equals("")) {
                    taskItem.setFinishDate(LocalDate.now().toString());
                    taskItem.setFinishTime((LocalTime.now().getHour()<10 ? "0"+(LocalTime.now().getHour()) : LocalTime.now().getHour())+":"+(LocalTime.now().getMinute()<10 ? "0"+(LocalTime.now().getMinute()) : LocalTime.now().getMinute()));
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

        autoCompleteTextView.setText(taskItem.getCategory());
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_category_item, categoryList);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String category = adapterView.getItemAtPosition(position).toString();
                taskItem.setCategory(category);
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