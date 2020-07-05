package com.gsapps.reminders.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static com.gsapps.reminders.util.CalendarUtils.getTodaysDateTimeinMillis;
import static java.time.ZoneId.systemDefault;
import static org.junit.Assert.assertEquals;

public class CalendarUtilsUnitTest {

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}


    @Test
    public void getTodaysDateTimeinMillis_Valid() {
        String actualDateTime = Instant.ofEpochMilli(getTodaysDateTimeinMillis())
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
}
