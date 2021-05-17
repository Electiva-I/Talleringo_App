package com.islam.talleringo.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.islam.talleringo.R;
import com.islam.talleringo.activities.MainActivity;

public class NotificationHandler extends ContextWrapper {
    private NotificationManager notificationManager;

    public static final  String CHANNEL_HIGH_ID = "1";
    public static final  String CHANNEL_HIGH_NAME = "HIGH CHANEL";

    public static final  String CHANNEL_LOW_ID = "2";
    public static final  String CHANNEL_LOW_NAME = "LOW CHANEL";

    private final int SUMMARY_GROUP_ID = 1001;
    private final  String SUMMARY_GROUP_NAME = "APP GROUP";


    public NotificationHandler(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels(){
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel highChannel = new NotificationChannel(
                    CHANNEL_HIGH_ID , CHANNEL_HIGH_NAME, NotificationManager.IMPORTANCE_HIGH);

            highChannel.setShowBadge(true);

            NotificationChannel lowChannel = new NotificationChannel(
                    CHANNEL_LOW_ID , CHANNEL_LOW_NAME, NotificationManager.IMPORTANCE_LOW);

            getNotificationManager().createNotificationChannel(highChannel);
            getNotificationManager().createNotificationChannel(lowChannel);
        }
    }

    public NotificationManager getNotificationManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public Notification.Builder createNotificationBuilder(String title, String message, boolean isHigh){
        if (Build.VERSION.SDK_INT >= 26) {
            if (isHigh) {
                return  this.createNotificationWithChannel(title, message, CHANNEL_HIGH_ID);
            }
            return  this.createNotificationWithChannel(title, message, CHANNEL_LOW_ID);
        }
        return this.createNotificationWithoutChannel(title, message);
    }

    private Notification.Builder createNotificationWithChannel(String title, String message, String channelId){
        if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O){

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("toMaintenance", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Notification.Action action = new Notification.Action.Builder(
                    Icon.createWithResource( this,android.R.drawable.ic_menu_save),
                    "Mark as Completed", pendingIntent).build();

            Notification.Action actionSkip = new Notification.Action.Builder(
                    Icon.createWithResource( this,android.R.drawable.ic_menu_save),
                    "Skip", pendingIntent).build();

            return new Notification.Builder(getApplicationContext(), channelId)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setGroup(SUMMARY_GROUP_NAME);
        }
        return  null;
    }

    private Notification.Builder createNotificationWithoutChannel(String title, String message){
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setAutoCancel(true);
    }

    public void publishNotificationSummaryGroup(boolean isHigh){
        if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O){
            String channelId = isHigh ?  CHANNEL_HIGH_ID : CHANNEL_LOW_ID;
            Notification summaryNotification =
                    new NotificationCompat.Builder(this, channelId)
                            .setContentTitle("ALGO")
                            //set content text to support devices running API level < 24
                            .setContentText("Two new messages")
                            .setSmallIcon(R.drawable.com_facebook_button_like_icon_selected)
                            //build summary info into InboxStyle template
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .addLine("Alex Faarborg  Check this out")
                                    .addLine("Jeff Chang    Launch Party")
                                    .setBigContentTitle("2 new messages")
                                    .setSummaryText("janedoe@example.com"))
                            //specify which group this notification belongs to
                            .setGroup(SUMMARY_GROUP_NAME)
                            //set this notification as the summary for the group
                            .setGroupSummary(true)
                            .build();
            getNotificationManager().notify(SUMMARY_GROUP_ID, summaryNotification);
        }
    }
}
