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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gsapps.reminders.services.LoadMeetingsTask;
import com.gsapps.reminders.services.MSAuthManager;

import static android.support.v4.content.LocalBroadcastManager.getInstance;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.gsapps.reminders.R.id.connect_with_outlook;
import static com.gsapps.reminders.R.id.meetings_view;
import static com.gsapps.reminders.R.layout.fragment_meetings;
import static com.gsapps.reminders.services.MSAuthManager.getAccessToken;
import static com.gsapps.reminders.util.Constants.ACTION_MSAL_ACCESS_TOKEN_ACQUIRED;
import static com.gsapps.reminders.util.ReminderUtils.isOutlookConnected;

public class MeetingsFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    public static View view; //TODO: 01-04-2019  To be changed to a better way to access view object in other classes.
    private Context context;
    private BroadcastReceiver msAuthReceiver;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        msAuthReceiver = new MSAuthReceiver();
        intentFilter = new IntentFilter(ACTION_MSAL_ACCESS_TOKEN_ACQUIRED);
        localBroadcastManager = getInstance(context);
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
        boolean isOutlookConnected = isOutlookConnected(context);
        toggleConnectOutlookMessage(isOutlookConnected);

        if(isOutlookConnected) {
            if(getAccessToken() == null) {
                MSAuthManager msAuthManager = new MSAuthManager();
                msAuthManager.loginOutlook(context);
            } else {
                getContactEvents();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(msAuthReceiver);
    }

    private void getContactEvents() {
        LoadMeetingsTask calendarTask = new LoadMeetingsTask((Activity) context);
        calendarTask.execute();
    }

    private void toggleConnectOutlookMessage(boolean isOutlookConnected) {
        if (isOutlookConnected) {
            view.findViewById(connect_with_outlook).setVisibility(GONE);
            view.findViewById(meetings_view).setVisibility(VISIBLE);
        } else {
            view.findViewById(connect_with_outlook).setVisibility(VISIBLE);
            view.findViewById(meetings_view).setVisibility(GONE);

        }
    }

    private class MSAuthReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            toggleConnectOutlookMessage(isOutlookConnected(context));
            getContactEvents();
        }
    }
}
