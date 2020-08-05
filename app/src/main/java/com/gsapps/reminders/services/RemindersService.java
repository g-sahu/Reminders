package com.gsapps.reminders.services;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.gsapps.reminders.model.CalendarDTO;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;

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
import static com.gsapps.reminders.factories.EventDTOFactory.createEventDTO;
import static com.gsapps.reminders.util.CalendarUtils.getCurrentTimeMillis;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.ContentProviderUtils.createContentProviderBundle;
import static com.gsapps.reminders.util.ContentProviderUtils.hasNext;
import static com.gsapps.reminders.util.ContentProviderUtils.query;
import static com.gsapps.reminders.util.ListUtils.asList;
import static com.gsapps.reminders.util.ListUtils.getOrDefault;
import static com.gsapps.reminders.util.enums.ContentProviderParam.SELECTION_ARGS;
import static java.lang.String.valueOf;

@RequiredArgsConstructor
public class RemindersService {
    private static final String LOG_TAG = RemindersService.class.getSimpleName();
    private final Context context;

    @WorkerThread
    public List<CalendarDTO> getCalendars(Bundle calendarsBundle) {
        List<CalendarDTO> calendars = new ArrayList<>();

        try (Cursor calendarsCursor = query(context.getContentResolver(), calendarsBundle)) {
            while (hasNext(calendarsCursor)) {
                CalendarDTO calendarDTO = new CalendarDTO();
                calendarDTO.setCalendarID(calendarsCursor.getInt(0));
                calendarDTO.setOwnerAccount(calendarsCursor.getString(1));
                calendars.add(calendarDTO);
            }
        }

        return calendars;
    }

    @WorkerThread
    public List<EventDTO> getEvents(CalendarType calendarType) {
        ArrayList<String> calendarsProjection = asList(_ID, OWNER_ACCOUNT);
        ArrayList<String> calendarsSelection = null;
        ArrayList<String> calendarsSelectionArgs = null;

        switch (calendarType) {
            case COMPREHENSIVE:
                calendarsSelection = asList(ACCOUNT_NAME + " = ? ", ACCOUNT_TYPE + " = ?");
                calendarsSelectionArgs = asList("simplygaurav07@gmail.com", "com.google");
                break;

            case CONTACT_EVENTS:
                calendarsSelection = asList(ACCOUNT_NAME + " = ? ", ACCOUNT_TYPE + " = ?", OWNER_ACCOUNT + " = ?");
                calendarsSelectionArgs = asList("simplygaurav07@gmail.com", "com.google", ADDRESS_BOOK_CONTACTS);
                break;

            default:
                Log.e(LOG_TAG, "Unrecognised calendar type: " + calendarType);
        }

        Bundle calendarsBundle = createContentProviderBundle(Calendars.CONTENT_URI, calendarsProjection, calendarsSelection, calendarsSelectionArgs, null);
        List<CalendarDTO> calendars = getCalendars(calendarsBundle);

        if (calendars.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<String> eventsProjection = asList(_ID, TITLE, DESCRIPTION, DTSTART);
        /*ArrayList<String> eventsSelection = asList(CALENDAR_ID + " = ?", DTSTART + " >= ?", DTSTART + " <= ?");
        ArrayList<String> eventSelectionArgs = asList(valueOf(getCurrentTimeMillis()), valueOf(getMidnightTimeMillis()));*/
        ArrayList<String> eventsSelection = asList(CALENDAR_ID + " = ?", DTSTART + " >= ?");
        ArrayList<String> eventSelectionArgs = asList(valueOf(getCurrentTimeMillis()));
        Bundle eventsBundle = createContentProviderBundle(Events.CONTENT_URI, eventsProjection, eventsSelection, eventSelectionArgs, null);
        return getEvents(calendars, eventsBundle);
    }

    @WorkerThread
    public List<EventDTO> getEvents(Collection<CalendarDTO> calendars, Bundle eventsBundle) {
        ArrayList<String> eventsSelectionArgs = getOrDefault(eventsBundle, SELECTION_ARGS.name(), new ArrayList<>());
        List<EventDTO> eventDTOs = new ArrayList<>();

        calendars.stream()
                 .forEach(calendar -> {
                     eventsSelectionArgs.add(0, valueOf(calendar.getCalendarID()));
                     eventsBundle.putStringArrayList(SELECTION_ARGS.name(), eventsSelectionArgs);
                     String ownerAccount = calendar.getOwnerAccount();

                     try (Cursor eventsCursor = query(context.getContentResolver(), eventsBundle)) {
                         while (hasNext(eventsCursor)) {
                             eventDTOs.add(populateEventDTO(ownerAccount, eventsCursor));
                         }
                     }

                     eventsSelectionArgs.remove(0);
                 });

        return eventDTOs;
    }

    private static EventDTO populateEventDTO(String ownerAccount, Cursor eventsCursor) {
        EventDTO eventDTO = createEventDTO(ownerAccount);
        eventDTO.setTitle(eventsCursor.getString(1));
        eventDTO.setEventDesc(eventsCursor.getString(2));
        eventDTO.setStartTs(eventsCursor.getLong(3));
        return eventDTO;
    }
}
