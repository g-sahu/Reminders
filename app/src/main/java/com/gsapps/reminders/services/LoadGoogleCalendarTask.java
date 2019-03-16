package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.event_list_view;
import static com.gsapps.reminders.model.Event.Frequency.ONCE;
import static com.gsapps.reminders.util.Constants.REQUEST_AUTHORIZATION;

public class LoadGoogleCalendarTask extends AsyncTask<Calendar, Void, Events> {
    private final String LOG_TAG = getClass().getSimpleName();
    final private Activity activity;

    public LoadGoogleCalendarTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Events doInBackground(Calendar... service) {
        Events events = null;

        try {
            events = service[0].events()
                               .list("primary")
                               .setOrderBy("startTime")
                               .setSingleEvents(true)
                               .setTimeMin(new DateTime("2018-01-01T00:00:00Z"))
                               .setTimeMax(new DateTime("2019-01-01T00:00:00Z"))
                               .execute();
        } catch (UserRecoverableAuthIOException ure) {
            activity.startActivityForResult(ure.getIntent(), REQUEST_AUTHORIZATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return events;
    }

    @Override
    protected void onPostExecute(Events events) {
        if(events != null) {
            List<Event> eventList = new ArrayList<>();

            for(com.google.api.services.calendar.model.Event item : events.getItems()) {
                Event event = new Event();
                event.setName(item.getSummary());
                event.setDesc(item.getDescription());
                event.setFrequency(ONCE);
                eventList.add(event);
            }

            Adapter eventListAdapter = new EventListAdapter(activity, eventList);
            RecyclerView eventListView = activity.findViewById(event_list_view);
            eventListView.setAdapter(eventListAdapter);
            eventListView.setLayoutManager(new LinearLayoutManager(activity));
        }
    }
}
