package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.util.comparators.StartDateComparator;
import com.microsoft.graph.extensions.Event;
import com.microsoft.graph.extensions.IEventCollectionPage;
import com.microsoft.graph.http.GraphServiceException;

import java.util.ArrayList;
import java.util.List;

import static com.gsapps.reminders.R.id.meetings_view;
import static com.gsapps.reminders.factories.EventDTOFactory.createEventDTO;
import static com.gsapps.reminders.services.GraphServiceClientManager.getInstance;
import static com.gsapps.reminders.util.ReminderUtils.getOptions;
import static com.gsapps.reminders.util.enums.EventType.MEETING_EVENT;
import static java.util.Collections.sort;

public class LoadMeetingsTask extends AsyncTask<Void, Void, Void> {
    private final String LOG_TAG = getClass().getSimpleName();
    private final Activity activity;
    private List<EventDTO> eventDTOList = new ArrayList<>();

    public LoadMeetingsTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            IEventCollectionPage result = getInstance()
                                            .getGraphServiceClient()
                                            .getMe()
                                            .getEvents()
                                            .buildRequest(getOptions())
                                            .get();

            for(Event meeting : result.getCurrentPage()) {
                EventDTO eventDTO = createEventDTO(MEETING_EVENT);
                //eventDTO.setSourceEventId(meeting.id);
                eventDTO.setTitle(meeting.subject);
                eventDTO.setEventDesc(meeting.bodyPreview);
                //eventDTO.setStartTs(getCalendar(meeting.start));
                //eventDTO.setEndTs(getCalendar(meeting.end));
                eventDTO.setRecurring(false);
                eventDTOList.add(eventDTO);
            }
        } catch (GraphServiceException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        sort(eventDTOList, new StartDateComparator());
        updateMyCalendarView();
    }

    private void updateMyCalendarView() {
        RecyclerView eventListView = activity.findViewById(meetings_view);
        eventListView.setAdapter(new EventListAdapter(activity, eventDTOList));
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
