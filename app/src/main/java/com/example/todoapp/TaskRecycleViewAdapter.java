package com.example.todoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.ViewHolder>{

    private ArrayList<TaskItem> taskItems;
    private Context context;
    private ToDoListDB toDoListDB;

    public TaskRecycleViewAdapter(ArrayList<TaskItem> taskItems, Context context) {
        this.taskItems = taskItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.toDoListDB = new ToDoListDB(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final TaskItem taskItem = taskItems.get(position);

        holder.textViewTitle.setText(taskItem.getTitle());
        holder.textViewDeadLineDate.setText(taskItem.getDeadLineDate());
        if(taskItem.getFinishDate().equals(""))
        {
            holder.doneButton.setBackgroundResource(R.drawable.ic_baseline_panorama_fish_eye_24);
            holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.textViewTitle.setTextColor(Color.parseColor("#000000"));
        }
        else
        {
            holder.doneButton.setBackgroundResource(R.drawable.ic_baseline_check_circle_24);
            holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textViewTitle.setTextColor(Color.parseColor("#676767"));
        }
        if(LocalDate.parse(taskItem.getDeadLineDate()).isBefore(LocalDate.now()))
        {
            holder.imageViewDeadLineDate.setImageResource(R.drawable.ic_baseline_event_24_red);
            holder.textViewDeadLineDate.setTextColor(Color.parseColor("#FF3232"));
        }

    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle,textViewDeadLineDate;
        Button doneButton;
        ImageView imageViewDeadLineDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTextView);
            textViewDeadLineDate = itemView.findViewById(R.id.deadLineTextView);
            doneButton = itemView.findViewById(R.id.doneButton);
            imageViewDeadLineDate = itemView.findViewById(R.id.deadLineImageView);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    TaskItem taskItem = taskItems.get(position);

                    if(taskItem.getFinishDate().equals("")) {
                        taskItem.setFinishDate(LocalDate.now().toString());
                        textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        textViewTitle.setTextColor(Color.parseColor("#676767"));
                        doneButton.setBackgroundResource(R.drawable.ic_baseline_check_circle_24);
                    }
                    else {
                        taskItem.setFinishDate("");
                        textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                        textViewTitle.setTextColor(Color.parseColor("#000000"));
                        doneButton.setBackgroundResource(R.drawable.ic_baseline_panorama_fish_eye_24);
                    }
                    toDoListDB.updateTask(taskItem);
                }
            });
        }
    }
}
