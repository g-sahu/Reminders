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

public class LoadGoogleCalendarTask extends AsyncTask<Void, Void, Void> {
    /*private static final String CALENDAR_LIST_FIELDS = "items/id";
    private static final String EVENTS_FIELDS = "timeZone, items/summary, items/start, items/description";
    private static final String INDIAN_HOLIDAY_CALENDAR = "en.indian#holiday@group.v.calendar.google.com";
    private static final String CONTACTS_CALENDAR = "addressbook#contacts@group.v.calendar.google.com";*/

    private final String LOG_TAG = getClass().getSimpleName();
    private final Activity activity;
    private List<EventDTO> eventDTOList = new ArrayList<>();

    public LoadGoogleCalendarTask(Activity activity) {
        this.activity = activity;
    }

    /*@Override
    protected Void doInBackground(Calendar... calendars) {
        try {
            CalendarList calendarList = getCalendarList(calendars[0]);

            for(CalendarListEntry calendarListEntry : calendarList.getItems()) {
                final String calendarId = calendarListEntry.getId();
                Events events = getEvents(calendars[0], calendarId);

                for (Event event : events.getItems()) {
                    EventDTO eventDTO = createEventDTO(calendarId);
                    eventDTO.setTitle(event.getSummary());
                    eventDTO.setEventDesc(event.getDescription());
                    eventDTO.setStartTs(getCalendar(event.getStart(), events.getTimeZone()));
                    eventDTOList.add(eventDTO);
                }
            }

            *//*if(isOutlookConnected(context)) {
                IEventCollectionPage result = getInstance()
                                                .getGraphServiceClient()
                                                .getMe()
                                                .getEvents()
                                                .buildRequest(getOptions())
                                                .get();

                for(com.microsoft.graph.extensions.Event meeting : result.getCurrentPage()) {
                    EventDTO eventDTO = new MeetingEventDTO();
                    //eventDTO.setEventId(meeting.id);
                    eventDTO.setTitle(meeting.subject);
                    eventDTO.setEventDesc(meeting.bodyPreview);
                    eventDTO.setStartTs(getCalendar(meeting.start));
                    eventDTO.setEndTs(getCalendar(meeting.end));
                    eventDTO.setRecurring(false);
                    eventDTOList.add(eventDTO);
                }
            }*//*
        } catch (UserRecoverableAuthIOException e) {
            activity.startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            Log.e(LOG_TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (GraphServiceException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }*/

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

    /*private CalendarList getCalendarList(Calendar calendar) throws IOException {
        return calendar.calendarList()
                       .list()
                       .setFields(CALENDAR_LIST_FIELDS)
                       .execute();
    }

    private Events getEvents(Calendar calendar, String calendarId) throws IOException {
        return calendar.events()
                       .list(calendarId)
                       .setFields(EVENTS_FIELDS)
                       .setSingleEvents(true)
                       .setTimeMin(new DateTime(getTodaysCalendar().getTimeInMillis()))
                       .execute();
    }

    private EventDTO createEventDTO(String calendarId) {
        final EventDTOFactory eventDTOFactory = getEventDTOFactory();
        EventDTO eventDTO;

        switch (calendarId) {
            case INDIAN_HOLIDAY_CALENDAR:
                eventDTO = eventDTOFactory.createEvent(HOLIDAY);
                break;

            case CONTACTS_CALENDAR:
                //String eventType = event.getGadget().getPreferences().get("goo.contactsEventType");
                eventDTO = eventDTOFactory.createEvent(CONTACT);
                break;

            default:
                eventDTO = eventDTOFactory.createEvent(CONTACT);
                // TODO: 15-04-2019 Add logic here to determine other types of events
                break;
        }

        return eventDTO;
    }*/
}
