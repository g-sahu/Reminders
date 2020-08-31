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
import static com.gsapps.reminders.R.id.my_calendar_view;
import static com.gsapps.reminders.R.layout.fragment_my_calendar;
import static com.gsapps.reminders.util.Constants.REQUEST_READ_CALENDAR;
import static com.gsapps.reminders.util.ReminderUtils.hasPermission;

public class MyCalendarFragment extends Fragment {
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

        return inflater.inflate(fragment_my_calendar, container, false);
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
        homeActivityViewModel.getMyCalendarEvents()
                             .observe(getViewLifecycleOwner(), this :: updateMyCalendarView);
    }

    private void updateMyCalendarView(List<EventDTO> eventDTOList) {
        RecyclerView eventListView = activity.findViewById(my_calendar_view);
        eventListView.setAdapter(new EventListAdapter(activity, eventDTOList));
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
