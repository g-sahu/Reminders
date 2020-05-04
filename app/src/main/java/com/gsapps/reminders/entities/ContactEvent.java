package com.gsapps.reminders.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.gsapps.reminders.converters.CalendarConverter;
import com.gsapps.reminders.converters.ContactEventTypeConverter;
import com.gsapps.reminders.model.enums.ContactEventType;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Entity(tableName = "contact_event",
        foreignKeys = @ForeignKey(entity = Event.class, parentColumns = "event_id", childColumns = "event_id"))
@Getter @Setter
public class ContactEvent {
    @PrimaryKey
    @ColumnInfo(name = "event_id")
    public long eventId;

    @NonNull
    @ColumnInfo(name = "src_event_id")
    public String sourceEventId;

    @NonNull
    @ColumnInfo(name = "contact_event_type")
    @TypeConverters(ContactEventTypeConverter.class)
    public ContactEventType contactEventType;

    @NonNull
    @ColumnInfo(name = "create_ts")
    @TypeConverters(CalendarConverter.class)
    public Calendar createTs;

    @ColumnInfo(name = "last_updt_ts")
    @TypeConverters(CalendarConverter.class)
    public Calendar lastUpdateTs;
}
