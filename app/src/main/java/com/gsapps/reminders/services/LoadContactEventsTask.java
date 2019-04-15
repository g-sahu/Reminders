package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Events;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.contact_events_view;
import static com.gsapps.reminders.model.Event.Frequency.ONCE;
import static com.gsapps.reminders.util.Constants.REQUEST_AUTHORIZATION;
import static com.gsapps.reminders.util.ReminderUtils.getCalendar;
import static com.gsapps.reminders.util.ReminderUtils.getEvent;
import static com.gsapps.reminders.util.ReminderUtils.getTodaysCalendar;

public class LoadContactEventsTask extends AsyncTask<com.google.api.services.calendar.Calendar, Void, Void> {
    private final String LOG_TAG = getClass().getSimpleName();
    final private Activity activity;
    private List<Event> eventList = new ArrayList<>();

    public LoadContactEventsTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(com.google.api.services.calendar.Calendar... service) {
        try {
            Events events = service[0].events()
                               .list("addressbook#contacts@group.v.calendar.google.com")
                               .setOrderBy("startTime")
                               .setSingleEvents(true)
                               .setTimeMin(new DateTime(getTodaysCalendar().getTimeInMillis()))
                               .execute();

            if(events != null) {
                for (com.google.api.services.calendar.model.Event item : events.getItems()) {
                    String eventType = item.getGadget().getPreferences().get("goo.contactsEventType");
                    Event event = getEvent(eventType);
                    event.setName(item.getSummary());
                    event.setDesc(item.getDescription());
                    event.setFrequency(ONCE);
                    event.setStartDate(getCalendar(item.getStart(), events.getTimeZone()));
                    eventList.add(event);
                }
            }
        } catch (UserRecoverableAuthIOException ure) {
            activity.startActivityForResult(ure.getIntent(), REQUEST_AUTHORIZATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        updateContactEventsView();
    }

    private void updateContactEventsView() {
        Adapter eventListAdapter = new EventListAdapter(activity, eventList);
        RecyclerView eventListView = activity.findViewById(contact_events_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
