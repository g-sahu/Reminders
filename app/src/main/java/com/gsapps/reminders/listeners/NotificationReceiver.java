package com.gsapps.reminders.listeners;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.gsapps.reminders.R;
import com.gsapps.reminders.activities.HomeActivity;

import static android.app.Notification.CATEGORY_SERVICE;
import static android.app.Notification.VISIBILITY_PUBLIC;
import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getActivity;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = NotificationReceiver.class.getSimpleName();
    private Context context;
    private static final String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(LOG_TAG, "Sending Reminders notification...");
        createNotification();
    }


    private void createNotification() {
        Intent openIntent = new Intent(context, HomeActivity.class);
        PendingIntent openPendingIntent = getActivity(context, 0, openIntent, FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        //builder.setStyle(mediaStyle);
        builder.setContentTitle("Event Title");
        builder.setContentText("Event Description");
        builder.setSubText("Event Date");
        builder.setVisibility(VISIBILITY_PUBLIC);
        builder.setSmallIcon(R.drawable.ic_one);
        builder.setShowWhen(false);
        builder.setContentIntent(openPendingIntent);
        builder.setCategory(CATEGORY_SERVICE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        //Building notification
        if (SDK_INT >= O) {
            createNotificationChannel(notificationManager);
            builder.setChannelId(CHANNEL_ID);
        }

        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

    @RequiresApi(api = O)
    private static void createNotificationChannel(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Event Notification", IMPORTANCE_HIGH);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }
}
