package com.gsapps.reminders.converters;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.gsapps.reminders.util.ReminderUtils.isNotNullOrEmpty;
import static java.util.Calendar.getInstance;

public class CalendarConverter {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @TypeConverter
    public static Calendar toCalendar(String value) {
        try {
            if(isNotNullOrEmpty(value)) {
                Calendar calendar = getInstance();
                calendar.setTime(sdf.parse(value));
                return calendar;
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unable to parse string: " + value);
        }

        return null;
    }

    @TypeConverter
    public static String fromCalendar(Calendar calendar) {
        return calendar == null ? null : sdf.format(calendar.getTimeInMillis());
    }
}
