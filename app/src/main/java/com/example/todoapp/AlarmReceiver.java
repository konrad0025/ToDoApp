package com.example.todoapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.style.ClickableSpan;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todoapp.database.ToDoListDB;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("notification",0);
        ToDoListDB toDoListDB = new ToDoListDB(context);
        TaskItem taskItem = toDoListDB.getTaskItem(id);
        Intent newIntent = new Intent(context, TaskDetailsActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        newIntent.putExtra("POSITION", taskItem);
        newIntent.putStringArrayListExtra("LIST_OF_CATEGORY", toDoListDB.getCategoryList());

        PendingIntent pendingIntent = PendingIntent.getActivity(context,taskItem.getKey_id(),newIntent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "foxandroid")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(taskItem.getTitle())
                .setContentText(taskItem.getDescription())
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(taskItem.getKey_id(),builder.build());
    }

}
