package com.islam.talleringo.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
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

    public Notification.Builder createNotificationBuilder(int notification_id,int maintenance_id, String title, String message, boolean isHigh){
        if (Build.VERSION.SDK_INT >= 26) {
            if (isHigh) {
                return  this.createNotificationWithChannel(notification_id, maintenance_id, title, message, CHANNEL_HIGH_ID);
            }
            return  this.createNotificationWithChannel(notification_id, maintenance_id, title, message, CHANNEL_LOW_ID);
        }
        return this.createNotificationWithoutChannel(title, message);
    }

    private Notification.Builder createNotificationWithChannel(int notification_id, int maintenance_id,String title, String message, String channelId){
        if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O){
            RemoteInput remoteInput = new RemoteInput.Builder("ASD")
                    .setLabel(getString(R.string.txt_notifications_cost_text))

                    .build();

            Intent intentDone = new Intent(this, NotificationActionService.class);
            intentDone.putExtra("notification_id", notification_id);
            intentDone.putExtra("maintenance_id",maintenance_id);
            intentDone.putExtra("type",0);

            Intent intentSkip = new Intent(this, NotificationActionService.class);
            intentSkip.putExtra("notification_id", notification_id);
            intentSkip.putExtra("maintenance_id",maintenance_id);
            intentSkip.putExtra("type",1);

            Intent intent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntentDone = PendingIntent.getService(this, 0, intentDone,  PendingIntent.FLAG_ONE_SHOT);
            PendingIntent pendingIntentSkip = PendingIntent.getService(this, 0, intentSkip,  PendingIntent.FLAG_ONE_SHOT);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0 );

            Notification.Action actionDone = new Notification.Action.Builder(
                    Icon.createWithResource( this,
                            android.R.drawable.ic_menu_save),
                            getString(R.string.txt_notifications_action_completed),
                    pendingIntentDone)
                    .addRemoteInput(remoteInput)
                    .build();

            Notification.Action actionSkip = new Notification.Action.Builder(
                    Icon.createWithResource( this,
                            android.R.drawable.ic_menu_save),
                            getString(R.string.txt_notifications_action_hold_over),
                            pendingIntentSkip).build();

            return new Notification.Builder(getApplicationContext(), channelId)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setActions(actionDone,actionSkip)
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
                            .setContentTitle(getString(R.string.app_name))
                            //set content text to support devices running API level < 24
                            .setContentText(getString(R.string.txt_notifications_summary_content))
                            .setSmallIcon(R.drawable.com_facebook_button_like_icon_selected)
                            //build summary info into InboxStyle template
                            .setStyle(new NotificationCompat.InboxStyle()
                                    .setBigContentTitle(getString(R.string.txt_notifications_summary_content_title))
                                    .setSummaryText(getString(R.string.menu_maintenance)))
                            //specify which group this notification belongs to
                            .setGroup(SUMMARY_GROUP_NAME)
                            //set this notification as the summary for the group
                            .setGroupSummary(true)
                            .build();
            getNotificationManager().notify(SUMMARY_GROUP_ID, summaryNotification);
        }
    }
}
