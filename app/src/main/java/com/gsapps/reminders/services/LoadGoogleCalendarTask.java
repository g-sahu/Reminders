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
import com.gsapps.reminders.model.MeetingEvent;
import com.gsapps.reminders.model.comparators.StartDateComparator;
import com.microsoft.graph.extensions.IEventCollectionPage;
import com.microsoft.graph.http.GraphServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.my_calendar_view;
import static com.gsapps.reminders.activities.HomeActivity.context;
import static com.gsapps.reminders.model.Event.Frequency.ONCE;
import static com.gsapps.reminders.model.EventFactory.getEventFactory;
import static com.gsapps.reminders.model.EventType.BIRTHDAY;
import static com.gsapps.reminders.model.EventType.HOLIDAY;
import static com.gsapps.reminders.services.GraphServiceClientManager.getInstance;
import static com.gsapps.reminders.util.Constants.REQUEST_AUTHORIZATION;
import static com.gsapps.reminders.util.ReminderUtils.*;
import static java.util.Collections.sort;

public class LoadGoogleCalendarTask extends AsyncTask<Calendar, Void, Void> {
    private final String LOG_TAG = getClass().getSimpleName();
    private final Activity activity;
    private List<Event> eventList = new ArrayList<>();

    public LoadGoogleCalendarTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Calendar... service) {
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
                                    .setTimeMin(new DateTime(getTodaysCalendar().getTimeInMillis()))
                                    .execute();

                for (com.google.api.services.calendar.model.Event item : events.getItems()) {
                    Event event = null;

                    if(calendarListEntry.getId().equals("en.indian#holiday@group.v.calendar.google.com")) {
                        event = getEventFactory().getEvent(HOLIDAY);
                    } else if(calendarListEntry.getId().equals("addressbook#contacts@group.v.calendar.google.com")) {
                        String eventType = item.getGadget().getPreferences().get("goo.contactsEventType");
                        event = getEvent(eventType);
                    } else {
                        event = getEventFactory().getEvent(BIRTHDAY);
                        // TODO: 15-04-2019 Add logic here to determine other types of events
                    }

                    event.setName(item.getSummary());
                    event.setDesc(item.getDescription());
                    event.setFrequency(ONCE);
                    event.setStartDate(getCalendar(item.getStart(), events.getTimeZone()));
                    eventList.add(event);
                }
            }

            if(isOutlookConnected(context)) {
                IEventCollectionPage result = getInstance()
                                                .getGraphServiceClient()
                                                .getMe()
                                                .getEvents()
                                                .buildRequest(getOptions())
                                                .get();

                for(com.microsoft.graph.extensions.Event meeting : result.getCurrentPage()) {
                    Event event = new MeetingEvent();
                    event.setId(meeting.id);
                    event.setName(meeting.subject);
                    event.setDesc(meeting.bodyPreview);
                    event.setStartDate(getCalendar(meeting.start));
                    event.setEndDate(getCalendar(meeting.end));
                    event.setFrequency(ONCE);
                    event.setRecurring(false);
                    eventList.add(event);
                }
            }
        } catch (UserRecoverableAuthIOException e) {
            activity.startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            Log.e(LOG_TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (GraphServiceException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        sort(eventList, new StartDateComparator());
        updateMyCalendarView();
    }

    private void updateMyCalendarView() {
        Adapter eventListAdapter = new EventListAdapter(activity, eventList);
        RecyclerView eventListView = activity.findViewById(my_calendar_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
