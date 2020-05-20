package com.gsapps.reminders.util;

import android.util.Log;
import com.microsoft.graph.extensions.DateTimeTimeZone;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;
import static java.util.TimeZone.getTimeZone;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class CalendarUtils {
    private static final String LOG_TAG = CalendarUtils.class.getSimpleName();

    static String getTodaysDateString(String format) {
         return getDateString(getTodaysCalendar(), format);
    }

    public static Calendar getTodaysCalendar() {
        Calendar calendar = getInstance();
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
        return calendar;
    }

    public static String getDateString(Calendar calendar, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format); // TODO: 09-04-2019 Convert start date to local date format
        return dateFormat.format(calendar.getTime());
    }

    public static Calendar getCalendar(DateTimeTimeZone dateTimeTimeZone) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = getInstance();

        try {
            Date date = dateFormat.parse(dateTimeTimeZone.dateTime);
            calendar.setTimeInMillis(date.getTime());
            calendar.setTimeZone(getTimeZone(dateTimeTimeZone.timeZone));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Exception while parsing date: ", e);
        }

        return calendar;
    }
}
