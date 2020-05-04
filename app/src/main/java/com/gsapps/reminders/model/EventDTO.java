package com.gsapps.reminders.model;

import androidx.annotation.Nullable;
import com.gsapps.reminders.model.enums.EventType;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

import static java.util.Objects.hash;

@Getter @Setter
public abstract class EventDTO {
    protected long eventId;
    protected String title;
    protected EventType eventType;
    protected String eventDesc;
    protected boolean isRecurring;
    protected Calendar startTs;
    protected Calendar endTs;
    protected Calendar createTs;
    protected Calendar lastUpdateTs;
    protected byte[] icon;

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof EventDTO) {
            EventDTO eventDTO = (EventDTO) obj;
            return eventId == eventDTO.eventId;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return hash(eventId);
    }

}
