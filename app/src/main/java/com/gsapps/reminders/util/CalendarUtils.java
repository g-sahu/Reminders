package com.gsapps.reminders.util;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.NoArgsConstructor;

import static java.lang.String.format;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class CalendarUtils {
    private static final String LOG_TAG = CalendarUtils.class.getSimpleName();
    private static final int MAX_DAY_OF_MONTH = 31;
    private static final int MIN_DAY_OF_MONTH = 1;

    public static LocalDateTime getStartOfDayDateTime() {
        return now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    public static long getLocalDateTimeMillis(LocalDateTime localDateTime) {
        return localDateTime.atZone(systemDefault())
                            .toInstant()
                            .toEpochMilli();
    }

    public static long getCurrentTimeMillis() {
        return Instant.now().toEpochMilli();
    }

    public static long getStartOfDayMillis() {
        return getLocalDateTimeMillis(getStartOfDayDateTime());
    }

    public static long getEndOfDayMillis() {
        return getLocalDateTimeMillis(getStartOfDayDateTime().plusDays(1));
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

    public static String getDateTimeString(long timeInMillis, String format, boolean isSuffixed) {
        final Instant instant = ofEpochMilli(timeInMillis);
        String date = instant.atZone(systemDefault())
                             .format(ofPattern(format));

        int dayOfMonth = ofInstant(instant, systemDefault()).getDayOfMonth();

        return isSuffixed
                ? format(date, getDayOfTheMonthSuffix(dayOfMonth))
                : date;
    }

    public static String getDateString(LocalDateTime localDateTime, String format) {
        return localDateTime.format(ofPattern(format));
    }

    public static String getStartOfDayDateTimeString(String format) {
        return getDateString(getStartOfDayDateTime(), format);
    }

    public static LocalDateTime getLocalDateTime(long timeInMillis) {
        return ofEpochMilli(timeInMillis)
                .atZone(systemDefault())
                .toLocalDateTime();
    }

    public static String getDayOfTheMonthSuffix(final int dayOfMonth) {
        if (dayOfMonth < MIN_DAY_OF_MONTH || dayOfMonth > MAX_DAY_OF_MONTH) {
            throw new IllegalArgumentException("Invalid day of month: " + dayOfMonth);
        }

        if (dayOfMonth >= 11 && dayOfMonth <= 13) {
            return "th";
        }

        switch (dayOfMonth % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }
}
