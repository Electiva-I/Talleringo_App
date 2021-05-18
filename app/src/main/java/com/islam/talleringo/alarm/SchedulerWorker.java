package com.islam.talleringo.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.islam.talleringo.R;
import com.islam.talleringo.notifications.NotificationHandler;

public class SchedulerWorker extends androidx.work.Worker {
    private static final String WORK_RESULT = "work_result";
    public SchedulerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        Data taskData = getInputData();
        String taskDataString = taskData.getString("title");
        /*showNotification("WorkManager", taskDataString != null ? taskDataString : "Message has been Sent");
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();*/
        NotificationHandler handler = new NotificationHandler(getApplicationContext());
        Notification.Builder builder = handler.createNotificationBuilder(taskDataString, "Notificacion de prueba", true);

        int min = 50;
        int max = 100;

        //Generate random int value from 50 to 100

        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        handler.getNotificationManager().notify(random_int, builder.build());
        handler.publishNotificationSummaryGroup(true);
        return Result.success();
    }
    private void showNotification(String task, String desc) {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "task_channel";
        String channelName = "task_name";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(1, builder.build());
    }
}