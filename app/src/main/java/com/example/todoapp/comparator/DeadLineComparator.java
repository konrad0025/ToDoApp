package com.example.todoapp.comparator;

import com.example.todoapp.TaskItem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

public class DeadLineComparator implements Comparator<TaskItem> {
    @Override
    public int compare(TaskItem taskItem, TaskItem t1) {
        int value = LocalDate.parse(taskItem.getDeadLineDate()).compareTo(LocalDate.parse(t1.getDeadLineDate()));
        if (value == 0) {
            value = LocalTime.parse(taskItem.getDeadLineTime()).compareTo(LocalTime.parse(t1.getDeadLineTime()));
        }
        return value;
    }
}
