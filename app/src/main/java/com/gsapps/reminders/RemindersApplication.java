package com.gsapps.reminders;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.gsapps.reminders.model.CalendarDTO;
import com.gsapps.reminders.services.LoadCalendarsTask;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_IN;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_US;
import static com.gsapps.reminders.util.ContentProviderUtils.createCalendarBundle;
import static com.gsapps.reminders.util.enums.CalendarType.COMPREHENSIVE;
import static com.gsapps.reminders.util.enums.CalendarType.CONTACT_EVENTS;
import static com.gsapps.reminders.util.enums.CalendarType.HOLIDAY;

public class RemindersApplication extends Application {
    private static final String LOG_TAG = RemindersApplication.class.getSimpleName();
    private static final Map<String, CalendarType> calendarTypeByOwnerAcMap;
    private final Map<CalendarType, Set<CalendarDTO>> calendarsByCalendarTypeMap = new EnumMap<>(CalendarType.class);

    static {
        calendarTypeByOwnerAcMap = new HashMap<>();
        calendarTypeByOwnerAcMap.put(ADDRESS_BOOK_CONTACTS, CONTACT_EVENTS);
        calendarTypeByOwnerAcMap.put(HOLIDAY_US, HOLIDAY);
        calendarTypeByOwnerAcMap.put(HOLIDAY_IN, HOLIDAY);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bundle calendarsBundle = createCalendarBundle(COMPREHENSIVE);

        try {
            new LoadCalendarsTask(this).execute(calendarsBundle)
                                       .get()
                                       .stream()
                                       .forEach(this :: updateCalendarMap);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOG_TAG, "Error while fetching calendars: " + e.getMessage());
        }
    }

    private void updateCalendarMap(CalendarDTO calendarDTO) {
        final String ownerAccount = calendarDTO.getOwnerAccount();

        if(calendarTypeByOwnerAcMap.containsKey(ownerAccount)) {
            CalendarType calendarType = calendarTypeByOwnerAcMap.get(ownerAccount);
            Set<CalendarDTO> calendars = calendarsByCalendarTypeMap.getOrDefault(calendarType, new HashSet<>());
            Set<CalendarDTO> allCalendars = calendarsByCalendarTypeMap.getOrDefault(COMPREHENSIVE, new HashSet<>());
            calendars.add(calendarDTO);
            allCalendars.add(calendarDTO);
            calendarsByCalendarTypeMap.put(calendarType, calendars);
            calendarsByCalendarTypeMap.put(COMPREHENSIVE, allCalendars);
        }
    }

    public Set<CalendarDTO> getCalendars(CalendarType calendarType) {
        return calendarsByCalendarTypeMap.getOrDefault(calendarType, new HashSet<>());
    }
}
