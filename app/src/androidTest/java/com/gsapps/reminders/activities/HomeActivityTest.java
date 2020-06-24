package com.gsapps.reminders.activities;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.close;
import static androidx.test.espresso.contrib.DrawerActions.open;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.gsapps.reminders.R.id.*;
import static com.gsapps.reminders.R.string.my_calendar;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {
    @Rule
    public ActivityScenarioRule<HomeActivity> activityRule = new ActivityScenarioRule<>(HomeActivity.class);

    // *** Navigation drawer ***
    @Test
    public void drawerLayout_displayed() {
        onView(withId(drawer_layout))
                .check(matches(isDisplayed()));
    }

    // *** Activity content ***
    @Test
    public void activityContent_displayed() {
        onView(withId(home_activity_content))
                .check(matches(isDisplayed()));
    }

    // *** Toolbar ***
    @Test
    public void toolbar_displayed() {
        onView(withId(toolbar))
                .check(matches(isDisplayed()));
    }

    @Test
    public void toolbar_title() {
        onView(withId(toolbar))
                .check(matches(hasDescendant(withText(my_calendar))));
    }

    // *** Fragment content ***
    @Test
    public void fragmentContent_displayed() {
        onView(withId(fragment_content))
                .check(matches(isDisplayed()));
    }

    @Test
    public void drawerLayout_open() {
        onView(withId(drawer_layout))
                .perform(open())
                .check(matches(isOpen()));
    }

    @Test
    public void drawerLayout_close() {
        onView(withId(drawer_layout))
                .perform(open())
                .perform(close())
                .check(matches(isClosed()));
    }

    // *** Navigation view ***
    @Test
    public void navView_displayed() {
        drawerLayout_open();

        onView(withId(nav_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void navView_notDisplayed() {
        onView(withId(nav_view))
                .check(matches(not(isDisplayed())));
    }

    // *** Navigation header ***
    @Test
    public void navHeader_displayed() {
        drawerLayout_open();

        onView(withId(nav_header))
                .check(matches(isDisplayed()));
    }

    @Test
    public void navHeader_notDisplayed() {
        onView(withId(nav_header))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void profilePic_displayed() {
        drawerLayout_open();

        onView(withId(profile_pic))
                .check(matches(isDisplayed()));
    }

    @Test
    public void profilePic_notDisplayed() {
        onView(withId(profile_pic))
                .check(matches(not(isDisplayed())));
    }

    // TODO: 15-06-2020 Fix failing test
    @Test @Ignore("Fix failing test")
    public void displayName_displayed() {
        drawerLayout_open();

        onView(withId(display_name))
                .check(matches(isDisplayed()));
    }

    @Test
    public void displayName_notDisplayed() {
        onView(withId(display_name))
                .check(matches(not(isDisplayed())));
    }

    // *** Navigation menu ***
    // TODO: 15-06-2020 Fix failing test
    @Test @Ignore("Fix failing test")
    public void navMenu_displayed() {
        drawerLayout_open();

        onView(withId(nav_menu))
                .check(matches(isDisplayed()));
    }
}
