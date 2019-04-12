package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.google.api.services.calendar.model.Events;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.Event;
import com.microsoft.graph.extensions.IEventCollectionPage;
import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.meetings_view;
import static com.gsapps.reminders.model.Event.Frequency.ONCE;
import static com.gsapps.reminders.services.GraphServiceClientManager.getInstance;
import static com.gsapps.reminders.util.ReminderUtils.getCalendar;
import static java.util.Collections.sort;

public class LoadMeetingsTask extends AsyncTask<Void, Void, List<Events>> {
    private final String LOG_TAG = getClass().getSimpleName();
    private final Activity activity;
    private List<Event> eventList = new ArrayList<>();

    public LoadMeetingsTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected List<Events> doInBackground(Void... params) {
        List<Events> eventsList = new ArrayList<>();

        try {
            IEventCollectionPage result = getInstance()
                                            .getGraphServiceClient()
                                            .getMe()
                                            .getEvents()
                                            .buildRequest(getQueryOptions())
                                            .get();

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
        } catch (GraphServiceException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return eventsList;
    }

    @Override
    protected void onPostExecute(List<Events> events) {
        super.onPostExecute(events);
        sort(eventList, new Event());
        updateMyCalendarView();
    }

    private void updateMyCalendarView() {
        Adapter eventListAdapter = new EventListAdapter(activity, eventList);
        RecyclerView eventListView = activity.findViewById(meetings_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private List<Option> getQueryOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new QueryOption("select", "subject,bodyPreview,start,end,location"));
        options.add(new QueryOption("filter", "start/dateTime ge '2019-04-12T08:00'"));
        options.add(new QueryOption("orderby", "start/dateTime"));
        return options;
    }
}