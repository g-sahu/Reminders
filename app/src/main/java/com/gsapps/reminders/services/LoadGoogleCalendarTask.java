package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Events;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.my_calendar_view;
import static com.gsapps.reminders.model.Event.Frequency.ONCE;
import static com.gsapps.reminders.util.Constants.REQUEST_AUTHORIZATION;
import static com.gsapps.reminders.util.ReminderUtils.getCalendar;
import static java.util.Collections.sort;

public class LoadGoogleCalendarTask extends AsyncTask<Calendar, Void, List<Events>> {
    private final String LOG_TAG = getClass().getSimpleName();
    final private Activity activity;

    public LoadGoogleCalendarTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Events> doInBackground(Calendar... service) {
        List<Events> eventsList = new ArrayList<>();

        try {
            CalendarList calendarList = service[0].calendarList()
                                                  .list()
                                                  .setFields("items/id")
                                                  .execute();

            for(CalendarListEntry calendarListEntry : calendarList.getItems()) {
                Events events = service[0].events()
                                   .list(calendarListEntry.getId())
                                   .setSingleEvents(true)
                                   .setTimeMin(new DateTime("2018-01-01T00:00:00Z"))
                                   .setTimeMax(new DateTime("2019-01-01T00:00:00Z"))
                                   .execute();

                eventsList.add(events);
            }
        } catch (UserRecoverableAuthIOException ure) {
            activity.startActivityForResult(ure.getIntent(), REQUEST_AUTHORIZATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eventsList;
    }

    @Override
    protected void onPostExecute(List<Events> eventsList) {
        List<Event> eventList = new ArrayList<>();

        for(Events events : eventsList) {
            for (com.google.api.services.calendar.model.Event item : events.getItems()) {
                Event event = new Event();
                event.setName(item.getSummary());
                event.setDesc(item.getDescription());
                event.setFrequency(ONCE);
                event.setStartDate(getCalendar(item.getStart(), events.getTimeZone()));
                eventList.add(event);
            }
        }

        sort(eventList, new Event());
        Adapter eventListAdapter = new EventListAdapter(activity, eventList);
        RecyclerView eventListView = activity.findViewById(my_calendar_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
