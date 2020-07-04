package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.util.comparators.StartDateComparator;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.List;

import lombok.RequiredArgsConstructor;

import static android.provider.CalendarContract.Calendars;
import static android.provider.CalendarContract.Calendars.ACCOUNT_NAME;
import static android.provider.CalendarContract.Calendars.ACCOUNT_TYPE;
import static android.provider.CalendarContract.Calendars.OWNER_ACCOUNT;
import static android.provider.CalendarContract.Calendars._ID;
import static android.provider.CalendarContract.Events;
import static android.provider.CalendarContract.Events.CALENDAR_ID;
import static android.provider.CalendarContract.Events.DESCRIPTION;
import static android.provider.CalendarContract.Events.DTSTART;
import static android.provider.CalendarContract.Events.TITLE;
import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.ContentProviderUtils.createContentProviderBundle;
import static java.util.Collections.sort;

@RequiredArgsConstructor
public class LoadEventsTask extends AsyncTask<CalendarType, Void, List<EventDTO>> {
    private static final String LOG_TAG = LoadEventsTask.class.getSimpleName();
    private final Context context;
    private final int viewId;

    @Override
    protected List<EventDTO> doInBackground(CalendarType... calendarType) {
        String[] PROJECTION_CALENDARS = {_ID, OWNER_ACCOUNT};
        String[] PROJECTION_EVENTS = {_ID, TITLE, DESCRIPTION, DTSTART};
        String calendarsSelection = null;
        String[] calendarsSelectionArgs = null;

        switch (calendarType[0]) {
            case ALL_CALENDAR:
                calendarsSelection = ACCOUNT_NAME + " = ? AND " + ACCOUNT_TYPE + " = ?";
                calendarsSelectionArgs = new String[]{"simplygaurav07@gmail.com", "com.google"};
                break;

            case CONTACT_EVENTS_CALENDAR:
                calendarsSelection = ACCOUNT_NAME + " = ? AND " + ACCOUNT_TYPE + " = ? AND " + OWNER_ACCOUNT + " = ?";
                calendarsSelectionArgs = new String[]{"simplygaurav07@gmail.com", "com.google", ADDRESS_BOOK_CONTACTS};
                break;

                default:
                    Log.e(LOG_TAG, "Unrecognised calendar type: " + calendarType[0]);
        }

        String eventsSelection = CALENDAR_ID + " = ? AND " + DTSTART + " >= ?";
        Bundle calendarsBundle = createContentProviderBundle(Calendars.CONTENT_URI, PROJECTION_CALENDARS, calendarsSelection, calendarsSelectionArgs, null);
        Bundle eventsBundle = createContentProviderBundle(Events.CONTENT_URI, PROJECTION_EVENTS, eventsSelection, null, null);
        return new RemindersService().getEventDTOs(context, calendarsBundle, eventsBundle);
    }

    @Override
    protected void onPostExecute(List<EventDTO> eventDTOList) {
        super.onPostExecute(eventDTOList);
        sort(eventDTOList, new StartDateComparator());
        updateMyCalendarView(eventDTOList);
    }

    private void updateMyCalendarView(List<EventDTO> eventDTOList) {
        Adapter eventListAdapter = new EventListAdapter(context, eventDTOList);
        RecyclerView eventListView = ((Activity) context).findViewById(viewId);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));
    }
}
