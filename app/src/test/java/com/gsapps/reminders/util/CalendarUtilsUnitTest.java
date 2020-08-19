package com.gsapps.reminders.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.gsapps.reminders.util.CalendarUtils.getEndOfDayMillis;
import static com.gsapps.reminders.util.CalendarUtils.getStartOfDayMillis;
import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.Assert.assertEquals;

public class CalendarUtilsUnitTest {

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}


    @Test
    public void getTodaysDateTimeinMillis_Valid() {
        String actualDateTime = Instant.ofEpochMilli(getStartOfDayMillis())
                                       .atZone(systemDefault())
                                       .toString();

        String expectedDateTime = Instant.now()
                                         .atZone(systemDefault())
                                         .withHour(0)
                                         .withMinute(0)
                                         .withSecond(0)
                                         .withNano(0)
                                         .toString();

        assertEquals(expectedDateTime, actualDateTime);
    }

    @Test
    public void getMidnightTimeMillis_Valid() {
        long actualMillis = getEndOfDayMillis();
        long expectedMillis = LocalDateTime.now()
                                           .truncatedTo(DAYS)
                                           .plusDays(1)
                                           .atZone(systemDefault())
                                           .toInstant()
                                           .toEpochMilli();

        assertEquals(expectedMillis, actualMillis);
    }
}
