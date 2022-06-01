package com.example.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        }
        else
        {
            holder.doneButton.setBackgroundResource(R.drawable.ic_baseline_check_circle_24);
        }
    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle,textViewDeadLineDate;
        Button doneButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTextView);
            textViewDeadLineDate = itemView.findViewById(R.id.deadLineTextView);
            doneButton = itemView.findViewById(R.id.doneButton);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    TaskItem taskItem = taskItems.get(position);

                    if(taskItem.getFinishDate().equals(""))
                    {
                        taskItem.setFinishDate("Now");
                        doneButton.setBackgroundResource(R.drawable.ic_baseline_check_circle_24);
                    }
                    else {
                        taskItem.setFinishDate("");
                        doneButton.setBackgroundResource(R.drawable.ic_baseline_panorama_fish_eye_24);
                    }
                    toDoListDB.updateTask(taskItem);
                }
            });
        }
    }
}
