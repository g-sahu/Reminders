package com.gsapps.reminders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Transaction;
import com.gsapps.reminders.dao.RemindersDao;
import com.gsapps.reminders.dao.RemindersDatabase;
import com.gsapps.reminders.entities.ContactEvent;
import com.gsapps.reminders.entities.Event;

import java.util.List;

import static com.gsapps.reminders.R.layout.fragment_test;
import static com.gsapps.reminders.util.enums.ContactEventType.BIRTHDAY;
import static com.gsapps.reminders.util.enums.EventType.CONTACT;
import static java.util.Calendar.getInstance;

public class TestFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RemindersDatabase database = RemindersDatabase.getDatabase(context);
        RemindersDao remindersDao = database.getRemindersDao();

        insertContactEvent(remindersDao);

        //Fetching all events
        List<Event> events = remindersDao.getEvents();
        List<ContactEvent> contactEvents = remindersDao.getContactEvents();
        return inflater.inflate(fragment_test, container, false);
    }

    @Transaction
    private void insertContactEvent(RemindersDao remindersDao) {
        //Inserting an Event
        Event event = new Event();
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
