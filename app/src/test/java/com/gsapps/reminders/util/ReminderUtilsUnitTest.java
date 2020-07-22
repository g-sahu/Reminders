package com.gsapps.reminders.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReminderUtilsUnitTest {

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void isNotNullOrEmpty_Null() {
        assertFalse(StringUtils.isNotNullOrEmpty(null));
    }

    @Test
    public void isNotNullOrEmpty_Empty() {
        assertFalse(StringUtils.isNotNullOrEmpty(""));
    }

    @Test
    public void isNotNullOrEmpty_WhiteSpaces() {
        assertFalse(StringUtils.isNotNullOrEmpty(" "));
    }

    @Test
    public void isNotNullOrEmpty_NonEmpty() {
        assertTrue(StringUtils.isNotNullOrEmpty("String"));
    }
}
