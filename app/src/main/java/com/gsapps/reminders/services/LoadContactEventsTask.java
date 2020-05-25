package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.factories.EventDTOFactory;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.util.comparators.StartDateComparator;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static android.provider.CalendarContract.Calendars.*;
import static android.provider.CalendarContract.Events;
import static android.provider.CalendarContract.Events.CALENDAR_ID;
import static android.provider.CalendarContract.Events.DESCRIPTION;
import static android.provider.CalendarContract.Events.DTSTART;
import static android.provider.CalendarContract.Events.TITLE;
import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.contact_events_view;
import static com.gsapps.reminders.factories.EventDTOFactory.getEventDTOFactory;
import static com.gsapps.reminders.util.CalendarUtils.getCalendar;
import static com.gsapps.reminders.util.CalendarUtils.getTodaysCalendar;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_IN;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_US;
import static com.gsapps.reminders.util.enums.EventType.CONTACT;
import static com.gsapps.reminders.util.enums.EventType.HOLIDAY;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;

@RequiredArgsConstructor
public class LoadContactEventsTask extends AsyncTask<Void, Void, List<EventDTO>> {
    private static final String LOG_TAG = LoadContactEventsTask.class.getSimpleName();
    private final Context context;
    private final List<EventDTO> eventDTOList = new ArrayList<>();
    private static final String[] PROJECTION_CALENDARS = {_ID, OWNER_ACCOUNT};
    private static final String[] PROJECTION_EVENTS = {_ID, TITLE, DESCRIPTION, DTSTART};

    @Override
    protected List<EventDTO> doInBackground(Void... voids) {
        ContentResolver contentResolver = context.getContentResolver();

        String calendarsSelection = ACCOUNT_NAME + " = ? AND " + ACCOUNT_TYPE + " = ? AND " + OWNER_ACCOUNT + " = ?";
        String[] calendarsSelectionArgs = {"simplygaurav07@gmail.com", "com.google", ADDRESS_BOOK_CONTACTS};
        String eventsSelection = "((" + CALENDAR_ID + " = ?) AND (" + DTSTART + " >= ?))";
        String todayTimeMillis = valueOf(getTodaysCalendar().getTimeInMillis());

        try (Cursor calendarsCursor = contentResolver.query(CONTENT_URI, PROJECTION_CALENDARS, calendarsSelection, calendarsSelectionArgs, null)) {
            while (calendarsCursor != null && calendarsCursor.moveToNext()) {
                long calID = calendarsCursor.getLong(0);
                String ownerAccount = calendarsCursor.getString(1);
                String[] eventsSelectionArgs = {valueOf(calID), todayTimeMillis};

                try (Cursor eventsCursor = contentResolver.query(Events.CONTENT_URI, PROJECTION_EVENTS, eventsSelection, eventsSelectionArgs, null)) {
                    while (eventsCursor != null && eventsCursor.moveToNext()) {
                        EventDTO eventDTO = createEventDTO(ownerAccount);

                        if (eventDTO != null) {
                            eventDTO.setTitle(eventsCursor.getString(1));
                            eventDTO.setEventDesc(eventsCursor.getString(2));
                            eventDTO.setStartTs(getCalendar(eventsCursor.getLong(3)));
                            eventDTOList.add(eventDTO);
                        }
                    }
                }
            }
        }

        return eventDTOList;
    }

    private EventDTO createEventDTO(String ownerAccount) {
        final EventDTOFactory eventDTOFactory = getEventDTOFactory();
        EventDTO eventDTO = null;

        switch (ownerAccount) {
            case ADDRESS_BOOK_CONTACTS:
                eventDTO = eventDTOFactory.createEventDTO(CONTACT);
                break;

            case HOLIDAY_IN:
            case HOLIDAY_US:
                eventDTO = eventDTOFactory.createEventDTO(HOLIDAY);
                break;
        }

        return eventDTO;
    }

    @Override
    protected void onPostExecute(List<EventDTO> eventDTOList) {
        super.onPostExecute(eventDTOList);
        sort(eventDTOList, new StartDateComparator());
        updateMyCalendarView(eventDTOList);
    }

    private void updateMyCalendarView(List<EventDTO> eventDTOList) {
        Adapter eventListAdapter = new EventListAdapter(context, eventDTOList);
        RecyclerView eventListView = ((Activity) context).findViewById(contact_events_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));
    }
}
