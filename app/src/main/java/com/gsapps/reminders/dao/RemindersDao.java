package com.gsapps.reminders.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.gsapps.reminders.entities.ContactEvent;
import com.gsapps.reminders.entities.Event;

import java.util.List;

@Dao
public interface RemindersDao {
    @Insert
    long insertEvent(Event event);

    @Insert
    long insertContactEvent(ContactEvent contactEvent);

    @Query("SELECT * FROM event ORDER BY event_id")
    List<Event> getEvents();

    @Query("SELECT * " +
            "FROM event e " +
            "INNER JOIN contact_event ce on e.event_id = ce.event_id " +
            "ORDER BY e.event_id")
    List<Event> getContactEvents();
}
