package com.gsapps.reminders.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gsapps.reminders.model.Event;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CALENDAR;
import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.CalendarContract.Calendars.*;
import static com.gsapps.reminders.R.layout.fragment_calendar;
import static com.gsapps.reminders.util.ReminderUtils.getContentFromProvider;
import static com.gsapps.reminders.util.ReminderUtils.hasPermission;

public class CalendarFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    private Context context;

    //Storage Permissions
    private static final int REQUEST_CALENDAR = 1;
    private static final String[] CALENDAR_PERMISSIONS = {READ_CALENDAR};
    private static final int READ_REQUEST_CODE = 42;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View calendarView = inflater.inflate(fragment_calendar, container, false);
        List<Event> eventList = new ArrayList<>();

        /*for(int i=0; i<5; i++) {
            Event event = new Event();
            event.setName("Event " + i);
            event.setDesc("Description for Event " + i);
            event.setFrequency(DAILY);
            event.setRecurring(true);
            eventList.add(event);
        }

        Adapter eventListAdapter = new EventListAdapter(context, eventList);
        RecyclerView eventListView = calendarView.findViewById(event_list_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));*/
        getCalendarEvents();
        return calendarView;
    }

    public void getCalendarEvents() {
        if(hasPermission(context, READ_CALENDAR)) {
            String[] projection = {_ID, ACCOUNT_NAME, CALENDAR_DISPLAY_NAME, OWNER_ACCOUNT};
            String selection = "";
            Uri[] uris = {CONTENT_URI};
            Cursor[] cursors = getContentFromProvider(context, uris, null, null, null, null);

            for (Cursor cursor: cursors) {
                try {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        while (!cursor.isAfterLast()) {
                            cursor.moveToNext();
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    cursor.close();
                }
            }
        } else {
            ActivityCompat.requestPermissions((Activity) context, CALENDAR_PERMISSIONS, REQUEST_CALENDAR);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_CALENDAR: {
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    //Do stuff
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (resultData != null) {
                // TODO: 11-03-2019 Show a message when user does not grant the permissions
            }
        }
    }

}
