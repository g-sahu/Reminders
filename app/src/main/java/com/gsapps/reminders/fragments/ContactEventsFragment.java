package com.gsapps.reminders.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.models.EventDTO;
import com.gsapps.reminders.viewmodels.HomeActivityViewModel;

import java.util.List;

import static android.Manifest.permission.READ_CALENDAR;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.gsapps.reminders.R.id.contact_events_view;
import static com.gsapps.reminders.R.layout.fragment_contact_events;
import static com.gsapps.reminders.util.ReminderUtils.hasPermission;

public class ContactEventsFragment extends Fragment {
    private static final String LOG_TAG = ContactEventsFragment.class.getSimpleName();
    private static final int REQUEST_READ_CALENDAR = 1;
    private HomeActivityViewModel homeActivityViewModel;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        homeActivityViewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(hasPermission(activity, READ_CALENDAR)) {
            subscribe();
        } else {
            requestPermissions(new String[]{READ_CALENDAR}, REQUEST_READ_CALENDAR);
        }

        return inflater.inflate(fragment_contact_events, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_READ_CALENDAR: {
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    subscribe();
                } else {
                    // TODO: 21-05-2020 Permission not granted. Show a message to the user
                }
            }
        }
    }

    private void subscribe() {
        homeActivityViewModel.getContactEvents().observe(getViewLifecycleOwner(), this :: updateContactEventsView);
    }

    private void updateContactEventsView(List<EventDTO> eventDTOList) {
        RecyclerView eventListView = activity.findViewById(contact_events_view);
        eventListView.setAdapter(new EventListAdapter(activity, eventDTOList));
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
