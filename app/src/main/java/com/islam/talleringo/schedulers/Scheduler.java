package com.islam.talleringo.schedulers;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.islam.talleringo.utils.App;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    public static boolean Schedule_Maintenance(String when, String tag, Data data){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(when));

        long alertTime = calendar.getTimeInMillis() -System.currentTimeMillis();

        WorkManager mWorkManager = WorkManager.getInstance();

        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(SchedulerWorker.class)
                .setInitialDelay(alertTime, TimeUnit.MILLISECONDS)
                .addTag(tag)
                .setInputData(data)
                .build();

        mWorkManager.enqueue(mRequest);

        return  true;
    }

    public static WorkManager getInstance(){
        return WorkManager.getInstance(App.getContext());
    }
}
