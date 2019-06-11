package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import com.google.api.services.calendar.model.Events;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.model.comparators.StartDateComparator;
import com.microsoft.graph.extensions.IEventCollectionPage;
import com.microsoft.graph.http.GraphServiceException;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.meetings_view;
import static com.gsapps.reminders.model.EventDTOFactory.getEventDTOFactory;
import static com.gsapps.reminders.model.enums.EventType.MEETING;
import static com.gsapps.reminders.services.GraphServiceClientManager.getInstance;
import static com.gsapps.reminders.util.ReminderUtils.getCalendar;
import static com.gsapps.reminders.util.ReminderUtils.getOptions;
import static java.util.Collections.sort;

public class LoadMeetingsTask extends AsyncTask<Void, Void, List<Events>> {
    private final String LOG_TAG = getClass().getSimpleName();
    private final Activity activity;
    private List<EventDTO> eventDTOList = new ArrayList<>();

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
                                            .buildRequest(getOptions())
                                            .get();

            for(com.microsoft.graph.extensions.Event meeting : result.getCurrentPage()) {
                EventDTO eventDTO = getEventDTOFactory().getEvent(MEETING);
                eventDTO.setEventId(meeting.id);
                eventDTO.setTitle(meeting.subject);
                eventDTO.setEventDesc(meeting.bodyPreview);
                eventDTO.setStartDate(getCalendar(meeting.start));
                eventDTO.setEndDate(getCalendar(meeting.end));
                eventDTO.setRecurring(false);
                eventDTOList.add(eventDTO);
            }
        } catch (GraphServiceException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return eventsList;
    }

    @Override
    protected void onPostExecute(List<Events> events) {
        super.onPostExecute(events);
        sort(eventDTOList, new StartDateComparator());
        updateMyCalendarView();
    }

    private void updateMyCalendarView() {
        Adapter eventListAdapter = new EventListAdapter(activity, eventDTOList);
        RecyclerView eventListView = activity.findViewById(meetings_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
