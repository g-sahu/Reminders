package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gsapps.reminders.R;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.model.EventDTOFactory;
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
import static com.gsapps.reminders.model.EventDTOFactory.getEventDTOFactory;
import static com.gsapps.reminders.util.enums.EventType.CONTACT;
import static com.gsapps.reminders.util.enums.EventType.HOLIDAY;
import static com.gsapps.reminders.util.CalendarUtils.getCalendar;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_IN;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_US;
import static java.lang.String.valueOf;
import static java.util.Collections.sort;

@RequiredArgsConstructor
public class LoadMyCalendarTask extends AsyncTask<Void, Void, List<EventDTO>> {
    private static final String LOG_TAG = LoadMyCalendarTask.class.getSimpleName();
    private final Context context;
    private final List<EventDTO> eventDTOList = new ArrayList<>();
    private static final String[] PROJECTION_CALENDARS = {_ID, OWNER_ACCOUNT};
    private static final String[] PROJECTION_EVENTS = {_ID, TITLE, DESCRIPTION, DTSTART};

    @Override
    protected List<EventDTO> doInBackground(Void... voids) {
        ContentResolver contentResolver = context.getContentResolver();

        String calendarsSelection = "((" + ACCOUNT_NAME + " = ?) AND (" + ACCOUNT_TYPE + " = ?))";
        String[] calendarsSelectionArgs = {"simplygaurav07@gmail.com", "com.google"};
        String eventsSelection = "((" + CALENDAR_ID + " = ?))";

        try (Cursor cursor = contentResolver.query(CONTENT_URI, PROJECTION_CALENDARS, calendarsSelection, calendarsSelectionArgs, null)) {
            while (cursor != null && cursor.moveToNext()) {
                long calID = cursor.getLong(0);
                String ownerName = cursor.getString(1);

                try (Cursor cursor1 = contentResolver.query(Events.CONTENT_URI, PROJECTION_EVENTS, eventsSelection, new String[]{valueOf(calID)}, null)) {
                    while (cursor1 != null && cursor1.moveToNext()) {
                        EventDTO eventDTO = createEventDTO(ownerName);

                        if (eventDTO != null) {
                            eventDTO.setTitle(cursor1.getString(1));
                            eventDTO.setEventDesc(cursor1.getString(2));
                            eventDTO.setStartTs(getCalendar(cursor1.getLong(3)));
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
                eventDTO = eventDTOFactory.createEvent(CONTACT);
                break;

            case HOLIDAY_IN:
            case HOLIDAY_US:
                eventDTO = eventDTOFactory.createEvent(HOLIDAY);
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
        RecyclerView eventListView = ((Activity) context).findViewById(R.id.my_calendar_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));
    }
}
