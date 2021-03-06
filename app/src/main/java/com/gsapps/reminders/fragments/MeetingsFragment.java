package com.gsapps.reminders.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gsapps.reminders.services.LoadMeetingsTask;
import com.gsapps.reminders.services.MSAuthManager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance;
import static com.gsapps.reminders.R.id.connect_with_outlook;
import static com.gsapps.reminders.R.id.meetings_view;
import static com.gsapps.reminders.R.layout.fragment_meetings;
import static com.gsapps.reminders.util.Constants.ACTION_MSAL_ACCESS_TOKEN_ACQUIRED;
import static com.gsapps.reminders.util.ReminderUtils.isOutlookConnected;

public class MeetingsFragment extends Fragment {
    private static final String LOG_TAG = MeetingsFragment.class.getSimpleName();
    private View view;
    private Activity activity;
    private BroadcastReceiver msAuthReceiver;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private MSAuthManager msAuthManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (Activity) getContext();
        msAuthReceiver = new MSAuthReceiver();
        msAuthManager = new MSAuthManager(activity);
        intentFilter = new IntentFilter(ACTION_MSAL_ACCESS_TOKEN_ACQUIRED);
        localBroadcastManager = getInstance(activity);
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
        boolean isOutlookConnected = isOutlookConnected(activity.getApplicationContext());
        toggleConnectOutlookMessage(isOutlookConnected);

        if(isOutlookConnected) {
            if(msAuthManager.getAccessToken() == null) {
                msAuthManager.loginOutlook(activity);
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
        new LoadMeetingsTask(activity, msAuthManager.getGraphServiceClient()).execute();
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
