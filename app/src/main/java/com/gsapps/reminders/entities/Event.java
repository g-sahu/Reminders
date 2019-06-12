package com.gsapps.reminders.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.gsapps.reminders.converters.BooleanConverter;
import com.gsapps.reminders.converters.CalendarConverter;
import com.gsapps.reminders.converters.EventTypeConverter;
import com.gsapps.reminders.model.enums.EventType;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

import static java.util.Objects.hash;

@Entity
@Getter @Setter
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    public long eventId;

    @NonNull
    public String title;

    @NonNull
    @ColumnInfo(name = "event_type")
    @TypeConverters(EventTypeConverter.class)
    public EventType eventType;

    @ColumnInfo(name = "event_desc")
    public String eventDesc;

    @NonNull
    @ColumnInfo(name = "is_recurring")
    @TypeConverters(BooleanConverter.class)
    public boolean isRecurring;

    @NonNull
    @ColumnInfo(name = "start_ts")
    @TypeConverters(CalendarConverter.class)
    public Calendar startTs;

    @NonNull
    @ColumnInfo(name = "end_ts")
    @TypeConverters(CalendarConverter.class)
    public Calendar endTs;

    @NonNull
    @ColumnInfo(name = "create_ts")
    @TypeConverters(CalendarConverter.class)
    public Calendar createTs;

    @ColumnInfo(name = "last_updt_ts")
    @TypeConverters(CalendarConverter.class)
    public Calendar lastUpdateTs;

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Event) {
            Event event = (Event) obj;
            return eventId == event.eventId;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return hash(eventId);
    }

}
