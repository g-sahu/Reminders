package com.gsapps.reminders.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gsapps.reminders.RemindersApplication;
import com.gsapps.reminders.listeners.NotificationReceiver;
import com.gsapps.reminders.models.EventDTO;
import com.gsapps.reminders.services.LoadCalendarsTask;
import com.gsapps.reminders.services.LoadEventsTask;
import com.gsapps.reminders.services.LoadEventsTask.Params;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CALENDAR;
import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.M;
import static com.gsapps.reminders.R.layout.activity_splash_screen;
import static com.gsapps.reminders.R.raw.msal_config;
import static com.gsapps.reminders.util.CalendarUtils.getEndOfDayMillis;
import static com.gsapps.reminders.util.CalendarUtils.getStartOfDayMillis;
import static com.gsapps.reminders.util.Constants.KEY_EVENTS;
import static com.gsapps.reminders.util.Constants.KEY_EVENTS_JSON;
import static com.gsapps.reminders.util.Constants.MSAL_ERROR_MSG;
import static com.gsapps.reminders.util.Constants.REQUEST_READ_CALENDAR;
import static com.gsapps.reminders.util.ContentProviderUtils.createCalendarBundle;
import static com.gsapps.reminders.util.JsonUtils.toJson;
import static com.gsapps.reminders.util.ReminderUtils.hasPermission;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;
import static com.gsapps.reminders.util.enums.CalendarType.COMPREHENSIVE;
import static com.microsoft.identity.client.IPublicClientApplication.ISingleAccountApplicationCreatedListener;
import static com.microsoft.identity.client.PublicClientApplication.createSingleAccountPublicClientApplication;
import static java.lang.String.format;
import static java.time.Duration.ofMinutes;

public class SplashScreenActivity extends Activity {
    private static final String LOG_TAG = SplashScreenActivity.class.getSimpleName();
    private RemindersApplication remindersApplication;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_splash_screen);
        activity = this;
        remindersApplication = (RemindersApplication) getApplication();
        createSingleAccountPublicClientApplication(this, msal_config, new PublicClientApplicationListener());
    }

    private void requestMissingPermissions() {
        if (!hasPermission(this, READ_CALENDAR)) {
            if (SDK_INT >= M) {
                requestPermissions(new String[]{READ_CALENDAR}, REQUEST_READ_CALENDAR);
            } else {
                // TODO: 14-09-2020 Check what needs to be done here
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_READ_CALENDAR: {
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    startupApp();
                } else {
                    // TODO: 21-05-2020 Permission not granted. Show a message to the user
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }
            }
        }
    }

    private void loadCalendars() {
        try {
            new LoadCalendarsTask(remindersApplication).execute(createCalendarBundle(COMPREHENSIVE))
                                                       .get()
                                                       .stream()
                                                       .forEach(calendarDTO -> remindersApplication.updateCalendarMap(calendarDTO));
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOG_TAG, "Error while fetching calendars: " + e.getMessage());
        }
    }

    private void registerNotificationIntents() {
        //Fetch events for the day
        try {
            new LoadEventsTask(remindersApplication).execute(new Params(COMPREHENSIVE, getStartOfDayMillis(), getEndOfDayMillis()))
                                                    .get()
                                                    .stream()
                                                    .forEach(this :: setAlarm);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOG_TAG, "Error while fetching events: " + e.getMessage());
        }
    }

    private void setAlarm(EventDTO eventDTO) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENTS_JSON, toJson(eventDTO));
        Intent intent = new Intent(this, NotificationReceiver.class).putExtra(KEY_EVENTS, bundle);
        PendingIntent pendingIntent = getBroadcast(this, 1, intent, FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(RTC_WAKEUP, eventDTO.getStartTs() - ofMinutes(30).toMillis(), pendingIntent);
    }

    private void startupApp() {
        loadCalendars();
        registerNotificationIntents();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    class PublicClientApplicationListener implements ISingleAccountApplicationCreatedListener {
        @Override
        public void onCreated(ISingleAccountPublicClientApplication application) {
            remindersApplication.setSingleAccountApp(application);
            requestMissingPermissions();
        }

        @Override
        public void onError(MsalException e) {
            String errorMsg = "Exception while creating public client.";
            Log.e(LOG_TAG, errorMsg + format(MSAL_ERROR_MSG, e.getErrorCode(), e.getMessage()));
            showToastMessage(activity, errorMsg);
            requestMissingPermissions();
        }
    }
}
