package com.gsapps.reminders.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gsapps.reminders.listeners.NotificationReceiver;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.services.RemindersService;

import java.util.List;

import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static android.provider.BaseColumns._ID;
import static android.provider.CalendarContract.Calendars;
import static android.provider.CalendarContract.Calendars.ACCOUNT_NAME;
import static android.provider.CalendarContract.Calendars.ACCOUNT_TYPE;
import static android.provider.CalendarContract.Calendars.OWNER_ACCOUNT;
import static android.provider.CalendarContract.Events;
import static android.provider.CalendarContract.Events.CALENDAR_ID;
import static android.provider.CalendarContract.Events.DESCRIPTION;
import static android.provider.CalendarContract.Events.DTSTART;
import static android.provider.CalendarContract.Events.TITLE;
import static com.gsapps.reminders.R.layout.activity_splash_screen;
import static com.gsapps.reminders.util.Constants.KEY_EVENTS;
import static com.gsapps.reminders.util.Constants.KEY_EVENTS_JSON;
import static com.gsapps.reminders.util.ContentProviderUtils.createContentProviderBundle;
import static com.gsapps.reminders.util.ReminderUtils.toJson;
import static java.time.Instant.now;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashScreenActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_splash_screen);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerNotificationIntents();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void registerNotificationIntents() {
        getEvents().stream()
                   .forEach(this :: setAlarm);
    }

    private void setAlarm(EventDTO eventDTO) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENTS_JSON, toJson(eventDTO));
        Intent intent = new Intent(this, NotificationReceiver.class).putExtra(KEY_EVENTS, bundle);
        PendingIntent pendingIntent = getBroadcast(this, 1, intent, FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(RTC_WAKEUP, now().plusSeconds(10).toEpochMilli(), pendingIntent);
        //alarmManager.set(RTC_WAKEUP, getLocalDateTimeinMillis(eventDTO.getStartTs().minusMinutes(30)), pendingIntent);
    }

    // TODO: 13-07-2020 Change this to fetch events for the day
    private List<EventDTO> getEvents() {
        String[] PROJECTION_CALENDARS = {_ID, OWNER_ACCOUNT};
        String[] PROJECTION_EVENTS = {_ID, TITLE, DESCRIPTION, DTSTART};
        String calendarsSelection = ACCOUNT_NAME + " = ? AND " + ACCOUNT_TYPE + " = ?";
        String[] calendarsSelectionArgs = new String[]{"simplygaurav07@gmail.com", "com.google"};
        String eventsSelection = CALENDAR_ID + " = ? AND " + DTSTART + " >= ?";
        Bundle calendarsBundle = createContentProviderBundle(Calendars.CONTENT_URI, PROJECTION_CALENDARS, calendarsSelection, calendarsSelectionArgs, null);
        Bundle eventsBundle = createContentProviderBundle(Events.CONTENT_URI, PROJECTION_EVENTS, eventsSelection, null, null);
        return new RemindersService().getEventDTOs(this, calendarsBundle, eventsBundle);
    }

}
