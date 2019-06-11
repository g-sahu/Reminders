package com.gsapps.reminders.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.gsapps.reminders.entities.Event;

import java.util.List;

@Dao
public interface RemindersDao {
    @Insert
    void insert(Event event);

    @Query("SELECT * FROM event ORDER BY event_id")
    List<Event> getAllEvents();
}
