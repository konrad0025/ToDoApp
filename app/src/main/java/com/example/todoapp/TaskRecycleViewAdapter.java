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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.database.ToDoListDB;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.ViewHolder>{

    private ArrayList<TaskItem> taskItems;
    private Context context;
    private ToDoListDB toDoListDB;
    private RecyclerViewClickListener listener;

    public TaskRecycleViewAdapter(ArrayList<TaskItem> taskItems, Context context, RecyclerViewClickListener listener) {
        this.taskItems = taskItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.toDoListDB = new ToDoListDB(context);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final TaskItem taskItem = taskItems.get(position);

        holder.textViewTitle.setText(taskItem.getTitle());
        holder.textViewDeadLineDate.setText(taskItem.getDeadLineDate()+" "+taskItem.getDeadLineTime());
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
        else
        {
            holder.imageViewDeadLineDate.setImageResource(R.drawable.ic_baseline_event_24_black);
            holder.textViewDeadLineDate.setTextColor(Color.parseColor("#000000"));
        }
        if(taskItem.getIsHidden().equals("true"))
        {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#DCDCDC"));
        }
        else
        {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }

    public void filterListUpdate(ArrayList<TaskItem> filteredList) {
        taskItems = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewTitle,textViewDeadLineDate;
        Button doneButton;
        ImageView imageViewDeadLineDate;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textViewTitle = itemView.findViewById(R.id.titleTextView);
            textViewDeadLineDate = itemView.findViewById(R.id.deadLineTextView);
            doneButton = itemView.findViewById(R.id.doneButton);
            imageViewDeadLineDate = itemView.findViewById(R.id.deadLineImageView);
            itemView.setOnClickListener(this);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    TaskItem taskItem = taskItems.get(position);

                    if(taskItem.getFinishDate().equals("")) {
                        taskItem.setFinishDate(LocalDate.now().toString());
                        taskItem.setFinishTime((LocalTime.now().getHour()<10 ? "0"+(LocalTime.now().getHour()) : LocalTime.now().getHour())+":"+(LocalTime.now().getMinute()<10 ? "0"+(LocalTime.now().getMinute()) : LocalTime.now().getMinute()));
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

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    public ArrayList<TaskItem> getTaskItems() {
        return taskItems;
    }
}
