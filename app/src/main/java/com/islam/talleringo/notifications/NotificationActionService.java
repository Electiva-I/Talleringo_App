package com.islam.talleringo.notifications;

import android.app.IntentService;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.islam.talleringo.database.AppDatabase;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.utils.App;

import java.util.Calendar;

public  class NotificationActionService extends IntentService {
    public NotificationActionService(String name) {
        super(name);
    }

    public NotificationActionService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle bundle = intent.getExtras();
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        float cost = 0;
        try {
            cost = Float.parseFloat(remoteInput.getCharSequence("ASD").toString());
        }catch (Exception e){
            cost = 0;
        }


        int notification_id = bundle.getInt("notification_id");
        int maintenance_id = bundle.getInt("maintenance_id");
        int type = bundle.getInt("type");

        switch (type){
            case 0:
                done(maintenance_id, cost);
                break;
            case 1:
                skip(maintenance_id);
                break;
        }

        NotificationHandler notificationHandler = new NotificationHandler(getBaseContext());
        notificationHandler.getNotificationManager().cancel(notification_id);
    }

    private void done(int maintenance_id, float cost){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        AppDatabase db = Room.databaseBuilder(App.getContext(),
                AppDatabase.class, "vehicle").allowMainThreadQueries().build();
        Maintenance maintenance = db.maintenanceDAO().getMaintenance(maintenance_id);

        Record record = new Record(maintenance.Vehicle_Id,maintenance.Detail, day+"/"+month+"/"+year, cost);

        db.maintenanceDAO().deleteId(maintenance.ID);
        db.recordDAO().insertAll(record);
    }

    private void skip(int maintenance_id){

    }

}
