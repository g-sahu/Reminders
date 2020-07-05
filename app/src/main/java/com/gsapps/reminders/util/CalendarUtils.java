package com.gsapps.reminders.util;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.NoArgsConstructor;

import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class CalendarUtils {
    private static final String LOG_TAG = CalendarUtils.class.getSimpleName();

    public static String getTodaysDateTimeString(String format) {
        return getDateString(getTodaysDateTime(), format);
    }

    public static LocalDateTime getTodaysDateTime() {
        return LocalDateTime.now()
                            .withHour(0)
                            .withMinute(0)
                            .withSecond(0)
                            .withNano(0);
    }

    public static long getTodaysDateTimeinMillis() {
        return getTodaysDateTime().atZone(systemDefault())
                                  .toInstant()
                                  .toEpochMilli();
    }

    /*public static Calendar getCalendar(DateTimeTimeZone dateTimeTimeZone) {
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
    }*/

    public static String getDateString(LocalDateTime localDateTime, String format) {
        return localDateTime.format(ofPattern(format));
    }

    public static LocalDateTime getLocalDateTime(long timeInMillis) {
        return Instant.ofEpochMilli(timeInMillis)
                      .atZone(systemDefault())
                      .toLocalDateTime();
    }
}
