package com.gsapps.reminders.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.gsapps.reminders.model.EventDTO;

import java.util.ArrayList;
import java.util.List;

import static com.gsapps.reminders.factories.EventDTOFactory.createEventDTO;
import static com.gsapps.reminders.util.CalendarUtils.getTodaysDateTimeMillis;
import static com.gsapps.reminders.util.ContentProviderUtils.SELECTION_ARGS;
import static com.gsapps.reminders.util.ContentProviderUtils.query;
import static java.lang.String.valueOf;

public class RemindersService {
    private static final String LOG_TAG = RemindersService.class.getSimpleName();

    public List<EventDTO> getEventDTOs(Context context, Bundle calendarsBundle, Bundle eventsBundle) {
        List<EventDTO> eventDTOList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        String todayTimeMillis = valueOf(getTodaysDateTimeMillis());

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
                            eventDTO.setStartTs(eventsCursor.getLong(3));
                            eventDTOList.add(eventDTO);
                        }
                    }
                }
            }
        }

        return eventDTOList;
    }
}
