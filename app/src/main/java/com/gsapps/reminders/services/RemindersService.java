package com.gsapps.reminders.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import com.gsapps.reminders.factories.EventDTOFactory;
import com.gsapps.reminders.model.EventDTO;

import java.util.ArrayList;
import java.util.List;

import static com.gsapps.reminders.factories.EventDTOFactory.getEventDTOFactory;
import static com.gsapps.reminders.util.CalendarUtils.getCalendar;
import static com.gsapps.reminders.util.CalendarUtils.getTodaysCalendar;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_IN;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_US;
import static com.gsapps.reminders.util.ContentProviderUtils.SELECTION_ARGS;
import static com.gsapps.reminders.util.ContentProviderUtils.query;
import static com.gsapps.reminders.util.enums.EventType.CONTACT;
import static com.gsapps.reminders.util.enums.EventType.HOLIDAY;
import static java.lang.String.valueOf;

public class RemindersService {
    private static final String LOG_TAG = RemindersService.class.getSimpleName();

    public List<EventDTO> getEventDTOs(Context context, Bundle calendarsBundle, Bundle eventsBundle) {
        List<EventDTO> eventDTOList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        String todayTimeMillis = valueOf(getTodaysCalendar().getTimeInMillis());

        try (Cursor calendarsCursor = query(contentResolver, calendarsBundle)) {
            while (calendarsCursor != null && calendarsCursor.moveToNext()) {
                long calID = calendarsCursor.getLong(0);
                String ownerAccount = calendarsCursor.getString(1);
                String[] eventsSelectionArgs = {valueOf(calID), todayTimeMillis};
                eventsBundle.putStringArray(SELECTION_ARGS, eventsSelectionArgs);

                try (Cursor eventsCursor = query(contentResolver, eventsBundle)) {
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

    private static EventDTO createEventDTO(String ownerAccount) {
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

            default:
                Log.e(LOG_TAG, "Unrecognised owner account: " + ownerAccount);
        }

        return eventDTO;
    }
}
