package com.gsapps.reminders.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.Event;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.extensions.IEventCollectionPage;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.meetings_view;
import static com.gsapps.reminders.activities.HomeActivity.context;
import static com.gsapps.reminders.fragments.MeetingsFragment.view;
import static com.gsapps.reminders.model.Event.Frequency.ONCE;
import static com.gsapps.reminders.util.ReminderUtils.getCalendar;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;

public class GraphEventCallbackListener implements ICallback<IEventCollectionPage> {
    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    public void success(final IEventCollectionPage result) {
        List<Event> eventList = new ArrayList<>();

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

        Adapter eventListAdapter = new EventListAdapter(context, eventList);
        RecyclerView eventListView = view.findViewById(meetings_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void failure(ClientException ex) {
        Log.e(LOG_TAG, ex.getMessage());
        showToastMessage(context, ex.getMessage());
    }
}
