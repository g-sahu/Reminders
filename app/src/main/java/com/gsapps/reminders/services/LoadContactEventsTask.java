package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import com.gsapps.reminders.model.EventDTO;

import java.util.ArrayList;
import java.util.List;

public class LoadContactEventsTask extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = LoadContactEventsTask.class.getSimpleName();
    final private Activity activity;
    private final List<EventDTO> eventDTOList = new ArrayList<>();

    public LoadContactEventsTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        //updateContactEventsView();
    }

    /*private void updateContactEventsView() {
        Adapter eventListAdapter = new EventListAdapter(activity, eventDTOList);
        RecyclerView eventListView = activity.findViewById(contact_events_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }*/
}
