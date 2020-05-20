package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.model.comparators.StartDateComparator;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.my_calendar_view;
import static java.util.Collections.sort;

public class LoadMyCalendarTask extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = LoadMyCalendarTask.class.getSimpleName();
    private final Activity activity;
    private final List<EventDTO> eventDTOList = new ArrayList<>();

    public LoadMyCalendarTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        sort(eventDTOList, new StartDateComparator());
        updateMyCalendarView();
    }

    private void updateMyCalendarView() {
        Adapter eventListAdapter = new EventListAdapter(activity, eventDTOList);
        RecyclerView eventListView = activity.findViewById(my_calendar_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
