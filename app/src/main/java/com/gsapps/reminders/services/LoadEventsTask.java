package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.CalendarDTO;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.util.comparators.StartDateComparator;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.ArrayList;
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
import static com.gsapps.reminders.adapters.EventListAdapter.Holder;
import static com.gsapps.reminders.util.CalendarUtils.getCurrentTimeMillis;
import static com.gsapps.reminders.util.CalendarUtils.getMidnightTimeMillis;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.ContentProviderUtils.createContentProviderBundle;
import static com.gsapps.reminders.util.ListUtils.asList;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;

@RequiredArgsConstructor
public class LoadEventsTask extends AsyncTask<CalendarType, Void, List<EventDTO>> {
    private static final String LOG_TAG = LoadEventsTask.class.getSimpleName();
    private final Context context;
    private final int viewId;
    private final RemindersService remindersService = new RemindersService();

    @Override
    protected List<EventDTO> doInBackground(CalendarType... calendarType) {
        ArrayList<String> calendarsProjection = asList(_ID, OWNER_ACCOUNT);
        ArrayList<String> calendarsSelection = null;
        ArrayList<String> calendarsSelectionArgs = null;

        switch (calendarType[0]) {
            case ALL_CALENDAR:
                calendarsSelection = asList(ACCOUNT_NAME + " = ? ", ACCOUNT_TYPE + " = ?");
                calendarsSelectionArgs = asList("simplygaurav07@gmail.com", "com.google");
                break;

            case CONTACT_EVENTS_CALENDAR:
                calendarsSelection = asList(ACCOUNT_NAME + " = ? ", ACCOUNT_TYPE + " = ?", OWNER_ACCOUNT + " = ?");
                calendarsSelectionArgs = asList("simplygaurav07@gmail.com", "com.google", ADDRESS_BOOK_CONTACTS);
                break;

                default:
                    Log.e(LOG_TAG, "Unrecognised calendar type: " + calendarType[0]);
        }

        Bundle calendarsBundle = createContentProviderBundle(Calendars.CONTENT_URI, calendarsProjection, calendarsSelection, calendarsSelectionArgs, null);
        List<CalendarDTO> calendars = remindersService.getCalendars(context, calendarsBundle);

        if (calendars.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<String> eventsProjection = asList(_ID, TITLE, DESCRIPTION, DTSTART);
        ArrayList<String> eventsSelection = asList(CALENDAR_ID + " = ?", DTSTART + " >= ?", DTSTART + " <= ?");
        ArrayList<String> eventSelectionArgs = asList(valueOf(getCurrentTimeMillis()), valueOf(getMidnightTimeMillis()));
        Bundle eventsBundle = createContentProviderBundle(Events.CONTENT_URI, eventsProjection, eventsSelection, eventSelectionArgs, null);
        return remindersService.getEvents(context, calendars, eventsBundle);
    }

    @Override
    protected void onPostExecute(List<EventDTO> eventDTOList) {
        super.onPostExecute(eventDTOList);
        sort(eventDTOList, new StartDateComparator());
        updateMyCalendarView(eventDTOList);
    }

    private void updateMyCalendarView(List<EventDTO> eventDTOList) {
        Adapter<Holder> eventListAdapter = new EventListAdapter(context, eventDTOList);
        RecyclerView eventListView = ((Activity) context).findViewById(viewId);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));
    }
}
