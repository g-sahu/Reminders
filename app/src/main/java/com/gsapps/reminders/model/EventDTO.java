package com.gsapps.reminders.model;

import com.gsapps.reminders.model.enums.Frequency;
import androidx.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

import static java.util.Objects.hash;

@Getter @Setter
public abstract class EventDTO {
    protected String eventId;
    protected String title;
    protected String eventDesc;
    protected Frequency frequency;
    protected boolean isRecurring;
    protected byte[] icon;
    protected Calendar startDate;
    protected Calendar endDate;

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof EventDTO) {
            EventDTO eventDTO = (EventDTO) obj;
            return eventId.equals(eventDTO.eventId);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return hash(eventId);
    }

}
