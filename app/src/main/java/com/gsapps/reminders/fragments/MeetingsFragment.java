package com.gsapps.reminders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.Event;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.core.IClientConfig;
import com.microsoft.graph.extensions.GraphServiceClient;
import com.microsoft.graph.extensions.IEventCollectionPage;
import com.microsoft.graph.extensions.IGraphServiceClient;

import java.util.ArrayList;
import java.util.List;

import static com.gsapps.reminders.R.id.meetings_view;
import static com.gsapps.reminders.R.layout.fragment_meetings;
import static com.gsapps.reminders.model.Event.Frequency.ONCE;
import static com.gsapps.reminders.services.GraphServiceClientManager.getInstance;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;
import static com.microsoft.graph.core.DefaultClientConfig.createWithAuthenticationProvider;
import static com.microsoft.graph.logger.LoggerLevel.Debug;

public class MeetingsFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    private Context context;
    private IGraphServiceClient mGraphServiceClient;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

        IClientConfig clientConfig = createWithAuthenticationProvider(getInstance());
        mGraphServiceClient = new GraphServiceClient.Builder().fromConfig(clientConfig).buildClient();
        mGraphServiceClient.getLogger().setLoggingLevel(Debug);
        getContactEvents();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(fragment_meetings, container, false);
        return view;
    }

    private void getContactEvents() {
        mGraphServiceClient.getMe().getEvents().buildRequest().get(new ICallback<IEventCollectionPage>() {
            @Override
            public void success(final IEventCollectionPage result) {
                List<Event> eventList = new ArrayList<>();

                for(com.microsoft.graph.extensions.Event meeting : result.getCurrentPage()) {
                    Event event = new Event();
                    event.setId(meeting.id);
                    event.setName(meeting.subject);
                    event.setDesc(meeting.bodyPreview);
                    //event.setStartDate(meeting.start);
                    //event.setEndDate(meeting.end);
                    event.setFrequency(ONCE);
                    event.setRecurring(false);
                    eventList.add(event);
                }

                RecyclerView.Adapter eventListAdapter = new EventListAdapter(context, eventList);
                RecyclerView eventListView = view.findViewById(meetings_view);
                eventListView.setAdapter(eventListAdapter);
                eventListView.setLayoutManager(new LinearLayoutManager(context));
            }

            @Override
            public void failure(ClientException ex) {
                Log.e(LOG_TAG, ex.getMessage());
                showToastMessage(context, ex.getMessage());
            }
        });
    }
}
