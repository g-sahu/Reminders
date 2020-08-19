package com.gsapps.reminders.services;

import android.app.Application;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.WorkerThread;

import com.gsapps.reminders.RemindersApplication;
import com.gsapps.reminders.model.CalendarDTO;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;

import static android.provider.BaseColumns._ID;
import static android.provider.CalendarContract.Calendars.ACCOUNT_NAME;
import static android.provider.CalendarContract.Calendars.ACCOUNT_TYPE;
import static android.provider.CalendarContract.Calendars.CALENDAR_DISPLAY_NAME;
import static android.provider.CalendarContract.Calendars.OWNER_ACCOUNT;
import static android.provider.CalendarContract.Events.DESCRIPTION;
import static android.provider.CalendarContract.Events.DTSTART;
import static android.provider.CalendarContract.Events.TITLE;
import static com.gsapps.reminders.factories.EventDTOFactory.createEventDTO;
import static com.gsapps.reminders.util.ContentProviderUtils.hasNext;
import static com.gsapps.reminders.util.ContentProviderUtils.query;
import static com.gsapps.reminders.util.ListUtils.getOrDefault;
import static com.gsapps.reminders.util.enums.ContentProviderParam.SELECTION_ARGS;
import static java.lang.String.valueOf;

@RequiredArgsConstructor
public class RemindersService {
    private static final String LOG_TAG = RemindersService.class.getSimpleName();
    private final Application application;

    @WorkerThread
    public List<CalendarDTO> getCalendars(Bundle calendarsBundle) {
        List<CalendarDTO> calendars = new ArrayList<>();

        try (Cursor calendarsCursor = query(application.getContentResolver(), calendarsBundle)) {
            while (hasNext(calendarsCursor)) {
                calendars.add(CalendarDTO.builder()
                                         .calendarID(calendarsCursor.getInt(calendarsCursor.getColumnIndex(_ID)))
                                         .calendarName(calendarsCursor.getString(calendarsCursor.getColumnIndex(CALENDAR_DISPLAY_NAME)))
                                         .accountName(calendarsCursor.getString(calendarsCursor.getColumnIndex(ACCOUNT_NAME)))
                                         .accountType(calendarsCursor.getString(calendarsCursor.getColumnIndex(ACCOUNT_TYPE)))
                                         .ownerAccount(calendarsCursor.getString(calendarsCursor.getColumnIndex(OWNER_ACCOUNT)))
                                         .build());
            }
        }

        return calendars;
    }

    @WorkerThread
    public List<EventDTO> getEvents(CalendarType calendarType, Bundle eventsBundle) {
        Set<CalendarDTO> calendars = ((RemindersApplication) application).getCalendars(calendarType);
        return calendars.isEmpty()
                ? new ArrayList<>()
                : getEvents(calendars, eventsBundle);
    }

    @WorkerThread
    public List<EventDTO> getEvents(Collection<CalendarDTO> calendars, Bundle eventsBundle) {
        ArrayList<String> eventsSelectionArgs = getOrDefault(eventsBundle.getStringArrayList(SELECTION_ARGS.name()), new ArrayList<>());
        List<EventDTO> eventDTOs = new ArrayList<>();

        calendars.stream()
                 .forEach(calendar -> {
                     eventsSelectionArgs.add(0, valueOf(calendar.getCalendarID()));
                     eventsBundle.putStringArrayList(SELECTION_ARGS.name(), eventsSelectionArgs);
                     String ownerAccount = calendar.getOwnerAccount();

                     try (Cursor eventsCursor = query(application.getContentResolver(), eventsBundle)) {
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
        eventDTO.setTitle(eventsCursor.getString(eventsCursor.getColumnIndex(TITLE)));
        eventDTO.setEventDesc(eventsCursor.getString(eventsCursor.getColumnIndex(DESCRIPTION)));
        eventDTO.setStartTs(eventsCursor.getLong(eventsCursor.getColumnIndex(DTSTART)));
        return eventDTO;
    }
}
