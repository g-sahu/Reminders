package com.gsapps.reminders.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.gsapps.reminders.util.ReminderUtils.*;
import static org.junit.Assert.*;

public class ReminderUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isNotNullOrEmpty_Null() {
        assertFalse(isNotNullOrEmpty(null));
    }

    @Test
    public void isNotNullOrEmpty_Empty() {
        assertFalse(isNotNullOrEmpty(""));
    }

    @Test
    public void isNotNullOrEmpty_WhiteSpaces() {
        assertFalse(isNotNullOrEmpty(" "));
    }

    @Test
    public void isNotNullOrEmpty_NonEmpty() {
        assertTrue(isNotNullOrEmpty("String"));
    }
}
