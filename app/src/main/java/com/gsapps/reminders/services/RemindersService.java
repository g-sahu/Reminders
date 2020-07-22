package com.gsapps.reminders.services;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.gsapps.reminders.model.CalendarDTO;
import com.gsapps.reminders.model.EventDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.gsapps.reminders.factories.EventDTOFactory.createEventDTO;
import static com.gsapps.reminders.util.ContentProviderUtils.hasNext;
import static com.gsapps.reminders.util.ContentProviderUtils.query;
import static com.gsapps.reminders.util.ListUtils.getOrDefault;
import static com.gsapps.reminders.util.enums.ContentProviderParam.SELECTION_ARGS;
import static java.lang.String.valueOf;

public class RemindersService {
    private static final String LOG_TAG = RemindersService.class.getSimpleName();

    public List<CalendarDTO> getCalendars(Context context, Bundle calendarsBundle) {
        List<CalendarDTO> calendars = new ArrayList<>();

        try (Cursor calendarsCursor = query(context.getContentResolver(), calendarsBundle)) {
            while (calendarsCursor != null && calendarsCursor.moveToNext()) {
                CalendarDTO calendarDTO = new CalendarDTO();
                calendarDTO.setCalendarID(calendarsCursor.getInt(0));
                calendarDTO.setOwnerAccount(calendarsCursor.getString(1));
                calendars.add(calendarDTO);
            }
        }

        return calendars;
    }

    public List<EventDTO> getEvents(Context context, Collection<CalendarDTO> calendars, Bundle eventsBundle) {
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
