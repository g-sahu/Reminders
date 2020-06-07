package com.gsapps.reminders.activities;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
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
import static com.gsapps.reminders.R.id.drawer_layout;
import static com.gsapps.reminders.R.id.nav_view;
import static com.gsapps.reminders.R.id.toolbar;
import static com.gsapps.reminders.R.string.my_calendar;

@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {
    @Rule
    public ActivityScenarioRule<HomeActivity> activityRule = new ActivityScenarioRule<>(HomeActivity.class);

    // Toolbar
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

    // Naviagtion drawer
    @Test
    public void drawerLayout_displayed() {
        onView(withId(drawer_layout))
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
        drawerLayout_open();

        onView(withId(drawer_layout))
                .perform(close())
                .check(matches(isClosed()));
    }

    @Test
    public void navView_displayed() {
        drawerLayout_open();

        onView(withId(nav_view))
                .check(matches(isDisplayed()));
    }
}
