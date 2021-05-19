package com.islam.talleringo.schedulers;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkerParameters;

import com.islam.talleringo.database.LiveData.DataViewModel;
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
        String title = taskData.getString("title");
        String detail = taskData.getString("detail");
        int maintenance_id = taskData.getInt("maintenance_id",0);
        /*showNotification("WorkManager", taskDataString != null ? taskDataString : "Message has been Sent");
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();*/

        int min = 0;
        int max = 1000 * 100;
        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);

        NotificationHandler handler = new NotificationHandler(getApplicationContext());
        Notification.Builder builder = handler.createNotificationBuilder(random_int, maintenance_id, title, detail, true);


        
        handler.getNotificationManager().notify(random_int, builder.build());
        handler.publishNotificationSummaryGroup(true);
        return Result.success();
    }
}