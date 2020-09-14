package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.models.EventDTO;
import com.gsapps.reminders.util.comparators.StartDateComparator;
import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.models.extensions.Event;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.IEventCollectionPage;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import static com.gsapps.reminders.R.id.meetings_view;
import static com.gsapps.reminders.factories.EventDTOFactory.createEventDTO;
import static com.gsapps.reminders.util.ReminderUtils.getOptions;
import static com.gsapps.reminders.util.enums.EventType.MEETING_EVENT;
import static java.util.Collections.sort;

@RequiredArgsConstructor
public class LoadMeetingsTask extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = LoadMeetingsTask.class.getSimpleName();
    private final List<EventDTO> eventDTOList = new ArrayList<>();
    private final Activity activity;
    private final IGraphServiceClient graphServiceClient;

    @Override
    protected Void doInBackground(Void... params) {
        try {
            IEventCollectionPage result = graphServiceClient.me()
                                                            .events()
                                                            .buildRequest(getOptions())
                                                            .select("subject,bodyPreview,createdDateTime,start,end,location")
                                                            .get();

            for(Event meeting : result.getCurrentPage()) {
                EventDTO eventDTO = createEventDTO(MEETING_EVENT);
                //eventDTO.setSourceEventId(meeting.id);
                eventDTO.setTitle(meeting.subject);
                eventDTO.setEventDesc(meeting.bodyPreview);
                //eventDTO.setStartTs(getCalendar(meeting.start));
                //eventDTO.setEndTs(getCalendar(meeting.end));
                // TODO: 13-07-2020 Enable field in EventDTO to remove this commented code
                //eventDTO.setRecurring(false);
                eventDTOList.add(eventDTO);
            }
        } catch (GraphServiceException e) {
            Log.e(LOG_TAG, "Error while fetching meeting events: " + e.getMessage());
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
