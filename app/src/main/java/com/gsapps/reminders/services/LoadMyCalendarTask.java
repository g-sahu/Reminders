package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.util.comparators.StartDateComparator;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
import static com.gsapps.reminders.R.id.my_calendar_view;
import static com.gsapps.reminders.util.ContentProviderUtils.createContentProviderBundle;
import static java.util.Collections.sort;

@RequiredArgsConstructor
public class LoadMyCalendarTask extends AsyncTask<Void, Void, List<EventDTO>> {
    private static final String LOG_TAG = LoadMyCalendarTask.class.getSimpleName();
    private final Context context;

    @Override
    protected List<EventDTO> doInBackground(Void... voids) {
        String[] PROJECTION_CALENDARS = {_ID, OWNER_ACCOUNT};
        String[] PROJECTION_EVENTS = {_ID, TITLE, DESCRIPTION, DTSTART};
        String calendarsSelection = ACCOUNT_NAME + " = ? AND " + ACCOUNT_TYPE + " = ?";
        String[] calendarsSelectionArgs = {"simplygaurav07@gmail.com", "com.google"};
        String eventsSelection = CALENDAR_ID + " = ? AND " + DTSTART + " >= ?";
        Bundle calendarsBundle = createContentProviderBundle(Calendars.CONTENT_URI, PROJECTION_CALENDARS, calendarsSelection, calendarsSelectionArgs, null);
        Bundle eventsBundle = createContentProviderBundle(Events.CONTENT_URI, PROJECTION_EVENTS, eventsSelection, null, null);
        RemindersService remindersService = new RemindersService();
        return remindersService.getEventDTOs(context, calendarsBundle, eventsBundle);
    }

    @Override
    protected void onPostExecute(List<EventDTO> eventDTOList) {
        super.onPostExecute(eventDTOList);
        sort(eventDTOList, new StartDateComparator());
        updateMyCalendarView(eventDTOList);
    }

    private void updateMyCalendarView(List<EventDTO> eventDTOList) {
        Adapter eventListAdapter = new EventListAdapter(context, eventDTOList);
        RecyclerView eventListView = ((Activity) context).findViewById(my_calendar_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));
    }
}
