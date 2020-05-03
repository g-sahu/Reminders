package com.gsapps.reminders.util;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import com.microsoft.graph.extensions.DateTimeTimeZone;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;
import static java.util.TimeZone.getTimeZone;

public class CalendarUtils {
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

    public static Calendar getCalendar(EventDateTime eventDateTime, String timezone) {
        DateTime dateTime = (eventDateTime.getDate() != null) ? eventDateTime.getDate() : eventDateTime.getDateTime();
        Calendar calendar = getInstance(getTimeZone(timezone));
        calendar.setTimeInMillis(dateTime.getValue());
        return calendar;
    }

    public static Calendar getCalendar(DateTimeTimeZone dateTimeTimeZone) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = dateFormat.parse(dateTimeTimeZone.dateTime);
            calendar.setTimeInMillis(date.getTime());
            calendar.setTimeZone(getTimeZone(dateTimeTimeZone.timeZone));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }
}
