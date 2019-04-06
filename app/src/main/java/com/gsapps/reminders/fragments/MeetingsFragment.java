package com.gsapps.reminders.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gsapps.reminders.listeners.GraphEventCallbackListener;
import com.gsapps.reminders.services.GraphServiceClientManager;
import com.microsoft.graph.extensions.IGraphServiceClient;

import static android.support.v4.content.LocalBroadcastManager.getInstance;
import static com.gsapps.reminders.R.layout.fragment_meetings;
import static com.gsapps.reminders.listeners.MSAuthCallbackListener.getAccessToken;
import static com.gsapps.reminders.services.MSAuthManager.loginOutlook;
import static com.gsapps.reminders.util.Constants.ACTION_MSAL_ACCESS_TOKEN_ACQUIRED;

public class MeetingsFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    public static View view; //TODO: 01-04-2019  To be changed to a better way to access view object in other classes.
    private Context context;
    private BroadcastReceiver msAuthReceiver;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private IGraphServiceClient mGraphServiceClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        msAuthReceiver = new MSAuthReceiver();
        intentFilter = new IntentFilter(ACTION_MSAL_ACCESS_TOKEN_ACQUIRED);
        localBroadcastManager = getInstance(context);
        GraphServiceClientManager graphServiceClientManager = new GraphServiceClientManager();
        mGraphServiceClient = graphServiceClientManager.getGraphServiceClient();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(fragment_meetings, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        localBroadcastManager.registerReceiver(msAuthReceiver, intentFilter);

        if(getAccessToken() == null) {
            loginOutlook(context);
        } else {
            getContactEvents();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(msAuthReceiver);
    }

    private void getContactEvents() {
        mGraphServiceClient.getMe()
                           .getEvents()
                           .buildRequest()
                           .get(new GraphEventCallbackListener());
    }

    private class MSAuthReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getContactEvents();
        }
    }
}
