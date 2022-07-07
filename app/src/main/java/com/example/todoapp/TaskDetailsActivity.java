package com.example.todoapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Toast;

import com.example.todoapp.database.ToDoListDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TaskDetailsActivity extends AppCompatActivity {

    EditText editTextTitle,editTextDeadLineDate,editTextCreateDate,editTextFinishDateDate,editTextDescription,editTextFile,editTextNotification;
    Button doneButton,deleteCategoryButton,deleteFileButton,deleteNotificationButton;
    TaskItem taskItem;
    private ToDoListDB toDoListDB = new ToDoListDB(this);
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    ArrayList<String> categoryList;
    Uri uri;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

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
        editTextNotification = findViewById(R.id.notificationEditText);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        doneButton = findViewById(R.id.doneButton);
        deleteCategoryButton = findViewById(R.id.deleteCategoryButton);
        deleteFileButton = findViewById(R.id.deleteFileButton);
        deleteNotificationButton = findViewById(R.id.deleteNotificationButton);
        taskDoneButtonHandle();
        deleteCategoryButtonHandle();
        deleteFileButtonHandle();
        deleteNotificationButtonHandle();
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
                        LocalTime deadLineTime = LocalTime.parse(taskItem.getDeadLineTime());
                        TimePickerDialog timePickerDialog = new TimePickerDialog(TaskDetailsActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                taskItem.setDeadLineDate(year + "-" + (month+1<10 ? "0"+(month+1) : (month+1)) + "-" + (day<10 ? "0"+day : day));
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
                if(taskItem.getFileName().equals(""))
                {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    activityResultLauncher.launch(intent);
                }
                else
                {
                    File file = new File(getFilesDir().getAbsolutePath()+"/"+taskItem.getFileName());
                    uri = FileProvider.getUriForFile(TaskDetailsActivity.this, getPackageName() + ".provider", file);
                    String type = getContentResolver().getType(uri);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(uri, type);
                    startActivity(intent);
                }
            }
        });

        deleteFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile(taskItem.getFileName());
                taskItem.setFileName("");
                editTextFile.setText("");
                deleteFileButtonHandle();
                toDoListDB.updateTask(taskItem);

            }
        });
        editTextFile.setText(taskItem.getFileName());

        createNotificationChannel();
        editTextNotification.setText(taskItem.getNotificationDate().equals("") ? "" : taskItem.getNotificationDate()+" "+taskItem.getNotificationTime());
        editTextNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskItem.getNotificationDate().equals(""))
                {
                    LocalDate notificationDate = LocalDate.now();
                    if(!taskItem.getNotificationDate().equals(""))
                    {
                        notificationDate = LocalDate.parse(taskItem.getNotificationDate());
                    }
                    DatePickerDialog datePickerDialog = new DatePickerDialog(TaskDetailsActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            LocalTime notificationTime = LocalTime.parse((LocalTime.now().getHour()<10 ? "0"+(LocalTime.now().getHour()) : LocalTime.now().getHour())+":"+(LocalTime.now().getMinute()<10 ? "0"+(LocalTime.now().getMinute()) : LocalTime.now().getMinute()));
                            if(!taskItem.getNotificationTime().equals(""))
                            {
                                notificationTime = LocalTime.parse(taskItem.getNotificationTime());
                            }

                            TimePickerDialog timePickerDialog = new TimePickerDialog(TaskDetailsActivity.this, android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                    taskItem.setNotificationDate(year + "-" + (month+1<10 ? "0"+(month+1) : (month+1)) + "-" + (day<10 ? "0"+day : day));

                                    taskItem.setNotificationTime((hour<10 ? "0"+(hour) : hour)+":"+(min<10 ? "0"+(min) : min));

                                    if(LocalDate.parse(taskItem.getNotificationDate()).compareTo(LocalDate.now()) < 0)
                                    {
                                        Toast.makeText(getApplicationContext(),"Can't select earlier date than now",Toast.LENGTH_SHORT).show();
                                        taskItem.setNotificationDate("");
                                        taskItem.setNotificationTime("");
                                    }
                                    else
                                    {
                                        if(LocalTime.parse(taskItem.getNotificationTime()).compareTo(LocalTime.now()) < 0 && LocalDate.parse(taskItem.getNotificationDate()).compareTo(LocalDate.now()) == 0)
                                        {
                                            Toast.makeText(getApplicationContext(),"Can't select earlier date than now",Toast.LENGTH_SHORT).show();
                                            taskItem.setNotificationDate("");
                                            taskItem.setNotificationTime("");
                                        }
                                        else
                                        {
                                            editTextNotification.setText(taskItem.getNotificationDate()+" "+taskItem.getNotificationTime());
                                            toDoListDB.updateTask(taskItem);
                                            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                            Intent intent = new Intent(TaskDetailsActivity.this,AlarmReceiver.class);
                                            intent.putExtra("notification", taskItem.getKey_id());
                                            pendingIntent = PendingIntent.getBroadcast(TaskDetailsActivity.this, taskItem.getKey_id(),intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                                            alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + neededTimeToNotification(),pendingIntent);
                                            deleteNotificationButtonHandle();
                                        }
                                    }
                                }
                            }, notificationTime.getHour(),notificationTime.getMinute(),true);
                            timePickerDialog.show();

                        }
                    }, notificationDate.getYear(), notificationDate.getMonthValue()-1, notificationDate.getDayOfMonth());
                    datePickerDialog.show();
                }
            }
        });
        deleteNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskItem.setNotificationTime("");
                taskItem.setNotificationDate("");
                editTextNotification.setText("");
                deleteNotificationButtonHandle();
                toDoListDB.updateTask(taskItem);

                Intent intent = new Intent(TaskDetailsActivity.this,AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(TaskDetailsActivity.this, taskItem.getKey_id(),intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                if(alarmManager == null)
                {
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                }
                alarmManager.cancel(pendingIntent);
            }
        });
    }

    private long neededTimeToNotification() {
        LocalDate localDate = LocalDate.parse(taskItem.getNotificationDate());
        LocalDateTime localDateTime = localDate.atTime(LocalTime.parse(taskItem.getNotificationTime()));
        long diff = ChronoUnit.MILLIS.between(LocalDateTime.now(), localDateTime);
        return (int)diff;
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        uri = data.getData();
                        if(uri != null)
                        {
                            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            String fileName = getFileName(uri);
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                File file = new File(getFilesDir().getAbsolutePath()+"/"+fileName);
                                file.createNewFile();
                                OutputStream outputStream = new FileOutputStream(file);
                                byte[] buf = new byte[1024];
                                int length;
                                while((length = inputStream.read(buf)) > 0){
                                    outputStream.write(buf, 0, length);
                                }
                                taskItem.setFileName(fileName);
                                toDoListDB.updateTask(taskItem);
                                deleteFileButtonHandle();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            editTextFile.setText(fileName);
                        }
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
    void deleteFileButtonHandle()
    {
        if(taskItem.getFileName().equals("")) {
            deleteFileButton.setVisibility(View.INVISIBLE);
        }
        else {
            deleteFileButton.setVisibility(View.VISIBLE);
        }
    }
    void deleteNotificationButtonHandle()
    {
        if(taskItem.getNotificationDate().equals("")) {
            deleteNotificationButton.setVisibility(View.INVISIBLE);
        }
        else {
            deleteNotificationButton.setVisibility(View.VISIBLE);
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

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "channel  Name";
            String description = "channel   Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}