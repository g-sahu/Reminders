package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Events;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.Event;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.extensions.IEventCollectionPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.my_calendar_view;
import static com.gsapps.reminders.activities.HomeActivity.context;
import static com.gsapps.reminders.model.Event.Frequency.ONCE;
import static com.gsapps.reminders.services.GraphServiceClientManager.getInstance;
import static com.gsapps.reminders.util.Constants.REQUEST_AUTHORIZATION;
import static com.gsapps.reminders.util.ReminderUtils.getCalendar;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;
import static java.util.Collections.sort;

public class LoadGoogleCalendarTask extends AsyncTask<Calendar, Void, List<Events>> implements
        ICallback<IEventCollectionPage> {
    private final String LOG_TAG = getClass().getSimpleName();
    private final Activity activity;
    private List<Event> eventList = new ArrayList<>();

    public LoadGoogleCalendarTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Events> doInBackground(Calendar... service) {
        List<Events> eventsList = new ArrayList<>();

        try {
            CalendarList calendarList = service[0]
                                            .calendarList()
                                            .list()
                                            .setFields("items/id")
                                            .execute();

            for(CalendarListEntry calendarListEntry : calendarList.getItems()) {
                Events events = service[0]
                                    .events()
                                    .list(calendarListEntry.getId())
                                    .setSingleEvents(true)
                                    .setTimeMin(new DateTime("2018-01-01T00:00:00Z"))
                                    .setTimeMax(new DateTime("2019-01-01T00:00:00Z"))
                                    .execute();

                eventsList.add(events);
            }

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

            getInstance()
                    .getGraphServiceClient()
                    .getMe()
                    .getEvents()
                    .buildRequest()
                    .get(this);
        } catch (UserRecoverableAuthIOException ure) {
            activity.startActivityForResult(ure.getIntent(), REQUEST_AUTHORIZATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eventsList;
    }

    /*@Override
    protected void onPostExecute(List<Events> eventsList) {
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
    }*/

    @Override
    public void success(final IEventCollectionPage result) {
        for(com.microsoft.graph.extensions.Event meeting : result.getCurrentPage()) {
            Event event = new Event();
            event.setId(meeting.id);
            event.setName(meeting.subject);
            event.setDesc(meeting.bodyPreview);
            event.setStartDate(getCalendar(meeting.start));
            event.setEndDate(getCalendar(meeting.end));
            event.setFrequency(ONCE);
            event.setRecurring(false);
            eventList.add(event);
        }

        sort(eventList, new Event());
        updateMyCalendarView();
    }

    @Override
    public void failure(ClientException ex) {
        Log.e(LOG_TAG, ex.getMessage());
        showToastMessage(context, ex.getMessage());
    }

    private void updateMyCalendarView() {
        Adapter eventListAdapter = new EventListAdapter(activity, eventList);
        RecyclerView eventListView = activity.findViewById(my_calendar_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
