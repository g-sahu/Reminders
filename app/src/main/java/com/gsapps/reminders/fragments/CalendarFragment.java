package com.gsapps.reminders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.Event;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.event_list_view;
import static com.gsapps.reminders.R.layout.fragment_calendar;
import static com.gsapps.reminders.model.Event.Frequency.DAILY;

public class CalendarFragment extends Fragment {
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View calendarView = inflater.inflate(fragment_calendar, container, false);
        List<Event> eventList = new ArrayList<>();

        for(int i=0; i<5; i++) {
            Event event = new Event();
            event.setName("Event " + i);
            event.setDesc("Description for Event " + i);
            event.setFrequency(DAILY);
            event.setRecurring(true);
            eventList.add(event);
        }

        Adapter eventListAdapter = new EventListAdapter(context, eventList);
        RecyclerView eventListView = calendarView.findViewById(event_list_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));
        return calendarView;
    }

}
