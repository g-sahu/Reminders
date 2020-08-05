package com.gsapps.reminders.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gsapps.reminders.listeners.NotificationReceiver;
import com.gsapps.reminders.model.EventDTO;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static com.gsapps.reminders.R.layout.activity_splash_screen;
import static com.gsapps.reminders.util.Constants.KEY_EVENTS;
import static com.gsapps.reminders.util.Constants.KEY_EVENTS_JSON;
import static com.gsapps.reminders.util.JsonUtils.toJson;
import static java.time.Instant.now;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashScreenActivity.class.getSimpleName();
    //private final RemindersService remindersService = new RemindersService();
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(activity_splash_screen);
    }

    @Override
    public void onStart() {
        super.onStart();
        //registerNotificationIntents();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*private void registerNotificationIntents() {
        for (EventDTO eventDTO : remindersService.getEvents(context, COMPREHENSIVE)) {
            setAlarm(eventDTO);
        }
    }*/

    private void setAlarm(EventDTO eventDTO) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENTS_JSON, toJson(eventDTO));
        Intent intent = new Intent(this, NotificationReceiver.class).putExtra(KEY_EVENTS, bundle);
        PendingIntent pendingIntent = getBroadcast(this, 1, intent, FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(RTC_WAKEUP, now().plusSeconds(10).toEpochMilli(), pendingIntent);
        //alarmManager.set(RTC_WAKEUP, getLocalDateTimeinMillis(eventDTO.getStartTs().minusMinutes(30)), pendingIntent);
    }
}
