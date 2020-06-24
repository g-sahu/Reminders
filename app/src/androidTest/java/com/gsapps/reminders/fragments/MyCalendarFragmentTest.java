package com.gsapps.reminders.fragments;

import android.view.View;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.gsapps.reminders.activities.HomeActivity;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static com.gsapps.reminders.R.drawable.holiday;
import static com.gsapps.reminders.R.drawable.outline_cake_black_18;
import static com.gsapps.reminders.R.id.*;
import static com.gsapps.reminders.utils.RecyclerViewMatchers.withViewAtPosition;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class MyCalendarFragmentTest {
    @Rule
    public ActivityScenarioRule<HomeActivity> activityRule = new ActivityScenarioRule<>(HomeActivity.class);
    private Matcher<View> itemContentMatcher, eventContentMatcher, myCalendarViewMatcher, moreOptionsMatcher, eventIconMatcher, eventNameMatcher, eventDateMatcher;

    @Before
    public void setUp() {
        itemContentMatcher = withId(item_content);
        eventContentMatcher = withId(event_content);
        myCalendarViewMatcher = withId(my_calendar_view);
        moreOptionsMatcher = withId(more_options);
        eventIconMatcher = withId(event_icon);
        eventNameMatcher = withId(event_name);
        eventDateMatcher = withId(event_date);
    }

    @Test
    public void defaultFragment_isDisplayed() {
        onView(withId(fragment_my_calendar))
                .check(matches(isDisplayed()));
    }

    @Test
    public void myCalendarView_isDisplayed() {
        onView(myCalendarViewMatcher)
                .check(matches(isDisplayed()));
    }

    @Test
    public void itemContent_hasDescendants() {
        onView(myCalendarViewMatcher)
                .check(matches(withViewAtPosition(0, itemContentMatcher)))
                .check(matches(withViewAtPosition(0, hasDescendant(eventContentMatcher))))
                .check(matches(withViewAtPosition(0, hasDescendant(moreOptionsMatcher))))
                .check(matches(withViewAtPosition(0, hasDescendant(hasDescendant(eventIconMatcher)))))
                .check(matches(withViewAtPosition(0, hasDescendant(hasDescendant(eventNameMatcher)))))
                .check(matches(withViewAtPosition(0, hasDescendant(hasDescendant(eventDateMatcher)))));
    }

    @Test
    public void myCalendarView_areDescendantsDisplayed() {
        onView(myCalendarViewMatcher)
                .check(matches(hasDescendant(allOf(itemContentMatcher, isDisplayed()))))
                .check(matches(hasDescendant(allOf(eventContentMatcher, isDisplayed()))))
                .check(matches(hasDescendant(allOf(moreOptionsMatcher, isDisplayed()))))
                .check(matches(hasDescendant(allOf(eventIconMatcher, isDisplayed()))))
                .check(matches(hasDescendant(allOf(eventNameMatcher, isDisplayed()))))
                .check(matches(hasDescendant(allOf(eventDateMatcher, isDisplayed()))));
    }

    @Test
    public void eventIcon_image() {
        onView(myCalendarViewMatcher)
                .check(matches(hasDescendant(allOf(eventIconMatcher, withTagValue(equalTo(holiday))))))
                .check(matches(hasDescendant(allOf(eventIconMatcher, withTagValue(equalTo(outline_cake_black_18))))));
    }

}
