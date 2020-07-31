package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.util.comparators.StartDateComparator;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.List;

import lombok.RequiredArgsConstructor;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.adapters.EventListAdapter.Holder;
import static java.util.Collections.sort;

@RequiredArgsConstructor
public class LoadEventsTask extends AsyncTask<CalendarType, Void, List<EventDTO>> {
    private static final String LOG_TAG = LoadEventsTask.class.getSimpleName();
    private final RemindersService remindersService = new RemindersService();
    private final Context context;
    private final int viewId;

    @Override
    protected List<EventDTO> doInBackground(CalendarType... calendarType) {
        return remindersService.getEvents(context, calendarType[0]);
    }

    @Override
    protected void onPostExecute(List<EventDTO> eventDTOList) {
        super.onPostExecute(eventDTOList);
        sort(eventDTOList, new StartDateComparator());
        updateMyCalendarView(eventDTOList);
    }

    private void updateMyCalendarView(List<EventDTO> eventDTOList) {
        Adapter<Holder> eventListAdapter = new EventListAdapter(context, eventDTOList);
        RecyclerView eventListView = ((Activity) context).findViewById(viewId);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));
    }
}
