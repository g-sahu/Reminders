package com.gsapps.reminders;

import android.app.Application;

import com.gsapps.reminders.models.CalendarDTO;
import com.gsapps.reminders.util.enums.CalendarType;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import static com.gsapps.reminders.R.string.app_name;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_IN;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_US;
import static com.gsapps.reminders.util.enums.CalendarType.COMPREHENSIVE;
import static com.gsapps.reminders.util.enums.CalendarType.CONTACT_EVENTS;
import static com.gsapps.reminders.util.enums.CalendarType.HOLIDAY;

public class RemindersApplication extends Application {
    private static final String LOG_TAG = RemindersApplication.class.getSimpleName();
    private static final Map<String, CalendarType> calendarTypeByOwnerAcMap;
    private final Map<CalendarType, Set<CalendarDTO>> calendarsByCalendarTypeMap = new EnumMap<>(CalendarType.class);
    @Getter @Setter private ISingleAccountPublicClientApplication singleAccountApp;
    @Getter private String appName;

    static {
        calendarTypeByOwnerAcMap = new HashMap<>();
        calendarTypeByOwnerAcMap.put(ADDRESS_BOOK_CONTACTS, CONTACT_EVENTS);
        calendarTypeByOwnerAcMap.put(HOLIDAY_US, HOLIDAY);
        calendarTypeByOwnerAcMap.put(HOLIDAY_IN, HOLIDAY);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appName = getString(app_name);
    }

    public void updateCalendarMap(CalendarDTO calendarDTO) {
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
