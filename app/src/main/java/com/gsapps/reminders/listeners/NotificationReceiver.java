package com.gsapps.reminders.listeners;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.gsapps.reminders.activities.HomeActivity;
import com.gsapps.reminders.models.ContactEventDTO;
import com.gsapps.reminders.models.EventDTO;
import com.gsapps.reminders.util.JsonUtils;

import static android.app.Notification.Builder;
import static android.app.Notification.CATEGORY_EVENT;
import static android.app.Notification.VISIBILITY_PUBLIC;
import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getActivity;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;
import static com.gsapps.reminders.R.drawable.ic_one;
import static com.gsapps.reminders.util.CalendarUtils.getDateTimeString;
import static com.gsapps.reminders.util.Constants.KEY_EVENTS;
import static com.gsapps.reminders.util.Constants.KEY_EVENTS_JSON;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = NotificationReceiver.class.getSimpleName();
    private static final String CHANNEL_ID = "1";
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i(LOG_TAG, "Sending Reminders notification...");
        Bundle bundle = intent.getBundleExtra(KEY_EVENTS);
        String json = bundle.getString(KEY_EVENTS_JSON);
        EventDTO eventDTO = JsonUtils.fromJson(json, ContactEventDTO.class);
        createNotification(eventDTO);
    }

    private void createNotification(EventDTO eventDTO) {
        // TODO: 13-07-2020 Change this to EventDetail activity
        Intent openIntent = new Intent(context, HomeActivity.class);
        PendingIntent openPendingIntent = getActivity(context, 0, openIntent, FLAG_UPDATE_CURRENT);
        Builder builder = new Builder(context);
        // TODO: 13-07-2020 Finalise on notification style 
        //builder.setStyle(mediaStyle);
        builder.setContentTitle(eventDTO.getTitle());
        builder.setContentText(eventDTO.getEventDesc());
        builder.setSubText(getDateTimeString(eventDTO.getStartTs(), "dd/MM/YYYY"));
        builder.setWhen(eventDTO.getStartTs());
        builder.setShowWhen(true);
        builder.setVisibility(VISIBILITY_PUBLIC);
        builder.setSmallIcon(ic_one);
        builder.setContentIntent(openPendingIntent);
        builder.setCategory(CATEGORY_EVENT);
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
        channel.setDescription("Channel for event notifications");
        notificationManager.createNotificationChannel(channel);
    }
}
