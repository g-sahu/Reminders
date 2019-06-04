package com.gsapps.reminders.model;

import com.gsapps.reminders.model.enums.Frequency;
import androidx.annotation.Nullable;

import java.util.Calendar;

import static java.util.Objects.hash;

//@Getter @Setter
public abstract class Event {
    protected String eventId;
    protected String title;
    protected String eventDesc;
    protected Frequency frequency;
    protected boolean isRecurring;
    protected byte[] icon;
    protected Calendar startDate;
    protected Calendar endDate;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Event) {
            Event event = (Event) obj;
            return eventId.equals(event.eventId);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return hash(eventId);
    }

}
