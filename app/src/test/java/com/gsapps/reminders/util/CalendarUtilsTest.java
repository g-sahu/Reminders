package com.gsapps.reminders.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static com.gsapps.reminders.util.CalendarUtils.getDateString;
import static com.gsapps.reminders.util.CalendarUtils.getTodaysCalendar;
import static java.util.Calendar.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SuppressWarnings("UseOfObsoleteDateTimeApi")
public class CalendarUtilsTest {
    private static final Calendar TODAYS_CALENDAR = getInstance();

    @Before
    public void setUp() {
        TODAYS_CALENDAR.set(HOUR_OF_DAY, 0);
        TODAYS_CALENDAR.set(MINUTE, 0);
        TODAYS_CALENDAR.set(SECOND, 0);
        TODAYS_CALENDAR.set(MILLISECOND, 0);
    }

    @After
    public void tearDown() {}

    @Test
    public void getTodaysDateString() {
    }

    @Test
    public void getTodaysCalendar_Null() {
        assertNotEquals(null, getTodaysCalendar());
    }

    @Test
    public void getTodaysCalendar_Now() {
        assertNotEquals(getInstance(), getTodaysCalendar());
    }

    @Test(expected = NullPointerException.class)
    public void getDateString_CalendarNull() {
        assertEquals("13/06/2020", getDateString(null, "dd/MM/yyyy"));
    }

    @Test(expected = NullPointerException.class)
    public void getDateString_FormatNull() {
        Calendar calendar = getInstance();
        calendar.set(2020, 05, 13); //Month is 0 based
        assertEquals("13/06/2020", getDateString(calendar, null));
    }

    @Test
    public void getDateString_Valid() {
        Calendar calendar = getInstance();
        calendar.set(2020, 05, 13); //Month is 0 based
        assertEquals("13/06/2020", getDateString(calendar, "dd/MM/yyyy"));
    }

    @Test
    public void getCalendar() {
    }

    @Test
    public void getCalendar1() {
    }
}
