package com.gsapps.reminders.services;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Events;
import com.gsapps.reminders.adapters.EventListAdapter;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.model.MeetingEventDTO;
import com.gsapps.reminders.model.comparators.StartDateComparator;
import com.microsoft.graph.extensions.IEventCollectionPage;
import com.microsoft.graph.http.GraphServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static com.gsapps.reminders.R.id.my_calendar_view;
import static com.gsapps.reminders.activities.HomeActivity.context;
import static com.gsapps.reminders.model.EventDTOFactory.getEventDTOFactory;
import static com.gsapps.reminders.model.enums.EventType.CONTACT;
import static com.gsapps.reminders.model.enums.EventType.HOLIDAY;
import static com.gsapps.reminders.services.GraphServiceClientManager.getInstance;
import static com.gsapps.reminders.util.Constants.REQUEST_AUTHORIZATION;
import static com.gsapps.reminders.util.ReminderUtils.getCalendar;
import static com.gsapps.reminders.util.ReminderUtils.getOptions;
import static com.gsapps.reminders.util.ReminderUtils.getTodaysCalendar;
import static com.gsapps.reminders.util.ReminderUtils.isOutlookConnected;
import static java.util.Collections.sort;

public class LoadGoogleCalendarTask extends AsyncTask<Calendar, Void, Void> {
    private final String LOG_TAG = getClass().getSimpleName();
    private final Activity activity;
    private List<EventDTO> eventDTOList = new ArrayList<>();

    public LoadGoogleCalendarTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Calendar... service) {
        try {
            CalendarList calendarList = service[0].calendarList()
                                                  .list()
                                                  .setFields("items/id")
                                                  .execute();

            for(CalendarListEntry calendarListEntry : calendarList.getItems()) {
                Events events = service[0].events()
                                          .list(calendarListEntry.getId())
                                          .setSingleEvents(true)
                                          .setTimeMin(new DateTime(getTodaysCalendar().getTimeInMillis()))
                                          .execute();

                for (com.google.api.services.calendar.model.Event item : events.getItems()) {
                    EventDTO eventDTO;

                    if(calendarListEntry.getId().equals("en.indian#holiday@group.v.calendar.google.com")) {
                        eventDTO = getEventDTOFactory().getEvent(HOLIDAY);
                    } else if(calendarListEntry.getId().equals("addressbook#contacts@group.v.calendar.google.com")) {
                        //String eventType = item.getGadget().getPreferences().get("goo.contactsEventType");
                        eventDTO = getEventDTOFactory().getEvent(CONTACT);
                    } else {
                        eventDTO = getEventDTOFactory().getEvent(CONTACT);
                        // TODO: 15-04-2019 Add logic here to determine other types of events
                    }

                    eventDTO.setTitle(item.getSummary());
                    eventDTO.setEventDesc(item.getDescription());
                    eventDTO.setStartDate(getCalendar(item.getStart(), events.getTimeZone()));
                    eventDTOList.add(eventDTO);
                }
            }

            if(isOutlookConnected(context)) {
                IEventCollectionPage result = getInstance()
                                                .getGraphServiceClient()
                                                .getMe()
                                                .getEvents()
                                                .buildRequest(getOptions())
                                                .get();

                for(com.microsoft.graph.extensions.Event meeting : result.getCurrentPage()) {
                    EventDTO eventDTO = new MeetingEventDTO();
                    eventDTO.setEventId(meeting.id);
                    eventDTO.setTitle(meeting.subject);
                    eventDTO.setEventDesc(meeting.bodyPreview);
                    eventDTO.setStartDate(getCalendar(meeting.start));
                    eventDTO.setEndDate(getCalendar(meeting.end));
                    eventDTO.setRecurring(false);
                    eventDTOList.add(eventDTO);
                }
            }
        } catch (UserRecoverableAuthIOException e) {
            activity.startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            Log.e(LOG_TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (GraphServiceException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

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
