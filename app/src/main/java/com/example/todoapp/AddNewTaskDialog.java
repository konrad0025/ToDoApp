package com.example.todoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.time.LocalDate;

public class AddNewTaskDialog extends AppCompatDialogFragment {

    private EditText editTextTaskTitle,editTextTaskDescription,editTextTaskDeadLineDate;
    private TaskDialogListener listener;
    private Context context;

    public AddNewTaskDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_new_task,null);

        builder.setView(view)
                .setTitle("New Task")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String taskTitle = editTextTaskTitle.getText().toString();
                        String taskDescription = editTextTaskDescription.getText().toString();
                        String taskDeadLineDate = editTextTaskDeadLineDate.getText().toString();
                        listener.applyTexts(taskTitle, taskDescription, taskDeadLineDate);
                    }
                });

        editTextTaskTitle = view.findViewById(R.id.titleEditText);
        editTextTaskDescription = view.findViewById(R.id.descriptionEditText);
        editTextTaskDeadLineDate = view.findViewById(R.id.deadLineDataPicker);
        editTextTaskDeadLineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate now = LocalDate.now();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editTextTaskDeadLineDate.setText(year + "-" + (month<10 ? "0"+month : month) + "-" + (day<10 ? "0"+day : day));
                    }
                }, now.getYear(), now.getMonthValue(), now.getDayOfMonth());
                datePickerDialog.show();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (TaskDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface TaskDialogListener{
        void applyTexts(String taskTitle, String taskDescription, String taskDeadLineDate);
    }
}

