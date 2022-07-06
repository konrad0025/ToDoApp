package com.example.todoapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

    EditText editTextTitle,editTextDeadLineDate,editTextCreateDate,editTextFinishDateDate,editTextDescription,editTextFile;
    Button doneButton,deleteCategoryButton;
    TaskItem taskItem;
    private ToDoListDB toDoListDB = new ToDoListDB(this);
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    ArrayList<String> categoryList;
    Uri uri;

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
        editTextFile = findViewById(R.id.fileEditText);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        doneButton = findViewById(R.id.doneButton);
        deleteCategoryButton = findViewById(R.id.deleteCategoryButton);
        taskDoneButtonHandle();
        deleteCategoryButtonHandle();
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

                taskDoneButtonHandle();
                toDoListDB.updateTask(taskItem);
            }
        });
        deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskItem.setCategory("");
                autoCompleteTextView.setText("");
                deleteCategoryButtonHandle();
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
                deleteCategoryButtonHandle();
                toDoListDB.updateTask(taskItem);
            }
        });

        editTextFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                activityResultLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        uri = data.getData();
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        editTextFile.setText(getFileName(uri));
                    }
                }
            });

    void taskDoneButtonHandle()
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
    void deleteCategoryButtonHandle()
    {
        if(taskItem.getCategory().equals("")) {
            deleteCategoryButton.setVisibility(View.INVISIBLE);
        }
        else {
            deleteCategoryButton.setVisibility(View.VISIBLE);
        }
    }

    //https://stackoverflow.com/questions/5568874/how-to-extract-the-file-name-from-uri-returned-from-intent-action-get-content
    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}