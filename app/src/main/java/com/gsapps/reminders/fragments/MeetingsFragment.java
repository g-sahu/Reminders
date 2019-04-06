package com.gsapps.reminders.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gsapps.reminders.listeners.GraphEventCallbackListener;
import com.gsapps.reminders.listeners.MSAuthCallbackListener;
import com.gsapps.reminders.services.GraphServiceClientManager;
import com.microsoft.graph.extensions.IGraphServiceClient;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import java.util.List;

import static android.support.v4.content.LocalBroadcastManager.getInstance;
import static com.gsapps.reminders.R.layout.fragment_meetings;
import static com.gsapps.reminders.listeners.MSAuthCallbackListener.getAccessToken;
import static com.gsapps.reminders.util.Constants.MS_AUTH_CLIENT_ID;

public class MeetingsFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    public static View view; //TODO: 01-04-2019  To be changed to a better way to access view object in other classes.
    private Context context;
    private BroadcastReceiver msAuthReceiver;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private IGraphServiceClient mGraphServiceClient;
    private final static String SCOPES [] = {"https://graph.microsoft.com/User.Read", "https://graph.microsoft.com/Calendars.Read"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        msAuthReceiver = new MSAuthReceiver();
        intentFilter = new IntentFilter("ACTION_TOKEN_ACQUIRED");
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
            PublicClientApplication sampleApp = new PublicClientApplication(context.getApplicationContext(), MS_AUTH_CLIENT_ID);

            try {
                List<User> users = sampleApp.getUsers();

                if (users != null && users.size() == 1) {
                    sampleApp.acquireTokenSilentAsync(SCOPES, users.get(0), new MSAuthCallbackListener());
                } else {
                    sampleApp.acquireToken((Activity) context, SCOPES, new MSAuthCallbackListener());
                }
            } catch (MsalClientException e) {
                Log.d(LOG_TAG, "MSAL Exception Generated while getting users: " + e.toString());
            }
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
