package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Events;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.EventDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.contact_events_view;
import static com.gsapps.reminders.model.EventDTOFactory.getEventDTOFactory;
import static com.gsapps.reminders.model.enums.EventType.CONTACT;
import static com.gsapps.reminders.util.CalendarUtils.getCalendar;
import static com.gsapps.reminders.util.CalendarUtils.getTodaysCalendar;
import static com.gsapps.reminders.util.Constants.REQUEST_AUTHORIZATION;

public class LoadContactEventsTask extends AsyncTask<com.google.api.services.calendar.Calendar, Void, Void> {
    private final String LOG_TAG = getClass().getSimpleName();
    final private Activity activity;
    private List<EventDTO> eventDTOList = new ArrayList<>();

    public LoadContactEventsTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(com.google.api.services.calendar.Calendar... service) {
        try {
            Events events = service[0].events()
                               .list("addressbook#contacts@group.v.calendar.google.com")
                               .setOrderBy("startTime")
                               .setSingleEvents(true)
                               .setTimeMin(new DateTime(getTodaysCalendar().getTimeInMillis()))
                               .execute();

            if(events != null) {
                for (com.google.api.services.calendar.model.Event item : events.getItems()) {
                    String eventType = item.getGadget().getPreferences().get("goo.contactsEventType");
                    EventDTO eventDTO = getEventDTOFactory().createEvent(CONTACT);
                    eventDTO.setTitle(item.getSummary());
                    eventDTO.setEventDesc(item.getDescription());
                    eventDTO.setStartTs(getCalendar(item.getStart(), events.getTimeZone()));
                    eventDTOList.add(eventDTO);
                }
            }
        } catch (UserRecoverableAuthIOException ure) {
            activity.startActivityForResult(ure.getIntent(), REQUEST_AUTHORIZATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        updateContactEventsView();
    }

    private void updateContactEventsView() {
        Adapter eventListAdapter = new EventListAdapter(activity, eventDTOList);
        RecyclerView eventListView = activity.findViewById(contact_events_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }
}
