package com.gsapps.reminders.entities;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.gsapps.reminders.model.enums.Frequency;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.hash;

@Entity
@Getter @Setter
public abstract class Event {
    @PrimaryKey(autoGenerate = true)
    protected int id;

    @ColumnInfo(name = "event_id")
    protected String eventId;

    protected String title;

    @ColumnInfo(name = "event_desc")
    protected String eventDesc;

    protected Frequency frequency;

    @ColumnInfo(name = "is_recurring")
    protected boolean isRecurring;

    @ColumnInfo(name = "start_ts")
    protected long startTs;

    @ColumnInfo(name = "end_ts")
    protected long endTs;

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Event) {
            Event event = (Event) obj;
            return id == event.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return hash(id);
    }

}
