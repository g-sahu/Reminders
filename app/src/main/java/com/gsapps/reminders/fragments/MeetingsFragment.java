package com.gsapps.reminders.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gsapps.reminders.listeners.GraphEventCallbackListener;
import com.gsapps.reminders.services.GraphServiceClientManager;
import com.microsoft.graph.extensions.IGraphServiceClient;

import static com.gsapps.reminders.R.layout.fragment_meetings;
import static com.microsoft.graph.logger.LoggerLevel.Debug;

public class MeetingsFragment extends Fragment {
    private IGraphServiceClient mGraphServiceClient;
    public static View view; //TODO: 01-04-2019  To be changed to a better way to access view object in other classes.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphServiceClientManager graphServiceClientManager = new GraphServiceClientManager();
        mGraphServiceClient = graphServiceClientManager.getGraphServiceClient();
        mGraphServiceClient.getLogger().setLoggingLevel(Debug);
        getContactEvents();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(fragment_meetings, container, false);
        return view;
    }

    private void getContactEvents() {
        mGraphServiceClient.getMe()
                           .getEvents()
                           .buildRequest()
                           .get(new GraphEventCallbackListener());
    }
}
