package com.gsapps.reminders.dao;

import android.content.Context;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.gsapps.reminders.entities.ContactEvent;
import com.gsapps.reminders.entities.Event;

import static androidx.room.Room.databaseBuilder;

@Database(entities = {Event.class, ContactEvent.class}, version = 1)
public abstract class RemindersDatabase extends RoomDatabase {
    private static volatile RemindersDatabase INSTANCE;

    public abstract RemindersDao getRemindersDao();

    public static RemindersDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RemindersDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = databaseBuilder(context.getApplicationContext(), RemindersDatabase.class, "reminders")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
