package com.gsapps.reminders.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;
import androidx.room.Transaction;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.gsapps.reminders.dao.RemindersDao;
import com.gsapps.reminders.dao.RemindersDatabase;
import com.gsapps.reminders.entities.ContactEvent;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.model.EventDTOFactory;
import com.gsapps.reminders.util.CalendarUtils;
import com.microsoft.graph.http.GraphServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.gsapps.reminders.R.string.app_name;
import static com.gsapps.reminders.dao.RemindersDatabase.getDatabase;
import static com.gsapps.reminders.model.EventDTOFactory.getEventDTOFactory;
import static com.gsapps.reminders.model.enums.ContactEventType.BIRTHDAY;
import static com.gsapps.reminders.model.enums.EventType.CONTACT;
import static com.gsapps.reminders.model.enums.EventType.HOLIDAY;
import static com.gsapps.reminders.util.CalendarUtils.getTodaysCalendar;
import static com.gsapps.reminders.util.ReminderUtils.getCalendar;
import static com.gsapps.reminders.util.ReminderUtils.getResourceString;
import static java.util.Calendar.getInstance;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String LOG_TAG = SyncAdapter.class.getSimpleName();
    private ContentResolver contentResolver;
    private Context context;
    private static final int READ_REQUEST_CODE = 42, REQUEST_ACCOUNT_PICKER = 3;
    private GoogleAccountCredential credential;
    private String accountName;
    private final List<EventDTO> eventDTOList = new ArrayList<>();

    private static final String CALENDAR_LIST_FIELDS = "items/id";
    private static final String EVENTS_FIELDS = "timeZone, items/summary, items/start, items/description";
    private static final String INDIAN_HOLIDAY_CALENDAR = "en.indian#holiday@group.v.calendar.google.com";
    private static final String CONTACTS_CALENDAR = "addressbook#contacts@group.v.calendar.google.com";

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the constructor maintains
     * compatibility with Android 3.0 and later platform versions
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(LOG_TAG, "Starting sync for Reminders app");

        try {
            Calendar calendar = getCalendar(credential, getResourceString(getContext(), app_name));
            CalendarList calendarList = getCalendarList(calendar);

            for(CalendarListEntry calendarListEntry : calendarList.getItems()) {
                final String calendarId = calendarListEntry.getId();
                Events events = getEvents(calendar, calendarId);

                for (Event event : events.getItems()) {
                    EventDTO eventDTO = createEventDTO(calendarId);
                    eventDTO.setTitle(event.getSummary());
                    eventDTO.setEventDesc(event.getDescription());
                    eventDTO.setStartTs(CalendarUtils.getCalendar(event.getStart(), events.getTimeZone()));
                    eventDTOList.add(eventDTO);
                }
            }

            RemindersDatabase database = getDatabase(context);
            RemindersDao remindersDao = database.getRemindersDao();
            insertContactEvent(remindersDao);

            //Fetching all events
            List<com.gsapps.reminders.entities.Event> events = remindersDao.getEvents();
            List<ContactEvent> contactEvents = remindersDao.getContactEvents();

            /*if(isOutlookConnected(context)) {
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
            }*/

            Log.i(LOG_TAG, "Sync completed for Reminders app");
        } catch (UserRecoverableAuthIOException e) {
            //context.startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);

            Log.e(LOG_TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (GraphServiceException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }


    /*private void updateMyCalendarView() {
        final RecyclerView.Adapter eventListAdapter = new EventListAdapter(activity, eventDTOList);
        final RecyclerView eventListView = activity.findViewById(my_calendar_view);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setLayoutManager(new LinearLayoutManager(activity));
    }*/

    private static CalendarList getCalendarList(Calendar calendar) throws IOException {
        return calendar.calendarList()
                       .list()
                       .setFields(CALENDAR_LIST_FIELDS)
                       .execute();
    }

    private static Events getEvents(Calendar calendar, String calendarId) throws IOException {
        return calendar.events()
                       .list(calendarId)
                       .setFields(EVENTS_FIELDS)
                       .setSingleEvents(true)
                       .setTimeMin(new DateTime(getTodaysCalendar().getTimeInMillis()))
                       .execute();
    }

    private static EventDTO createEventDTO(final String calendarId) {
        final EventDTOFactory eventDTOFactory = getEventDTOFactory();
        final EventDTO eventDTO;

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
    }

    @Transaction
    private void insertContactEvent(RemindersDao remindersDao) {
        //Inserting an Event
        com.gsapps.reminders.entities.Event event = new com.gsapps.reminders.entities.Event();
        event.setTitle("My new Event");
        event.setEventType(CONTACT);
        event.setEventDesc("Description for event.");
        event.setRecurring(false);
        event.setStartTs(getInstance());
        event.setEndTs(getInstance());
        event.setCreateTs(getInstance());
        long eventId = remindersDao.insertEvent(event);

        ContactEvent contactEvent = new ContactEvent();
        contactEvent.setEventId(eventId);
        contactEvent.setSourceEventId("absddfsd");
        contactEvent.setContactEventType(BIRTHDAY);
        contactEvent.setCreateTs(getInstance());
        remindersDao.insertContactEvent(contactEvent);
    }
}
