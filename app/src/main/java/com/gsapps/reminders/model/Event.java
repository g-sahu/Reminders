package com.gsapps.reminders.model;

import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Comparator;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.KITKAT;
import static java.util.Objects.hash;

//@Getter @Setter
public class Event implements Comparator<Object> {
    protected String id;
    protected String name;
    protected String desc;
    protected Frequency frequency;
    protected boolean isRecurring;
    protected byte[] icon;
    protected Calendar startDate;
    protected Calendar endDate;

    public enum Frequency {
        YEARLY, HALF_YEARLY, QUARTERLY, MONTHLY, FORTNIGHTLY, WEEKLY, DAILY, ONCE
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
            return id.equals(event.id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (SDK_INT >= KITKAT) {
            return hash(id);
        }

        return super.hashCode();
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        Event event1 = (Event) lhs;
        Event event2 = (Event) rhs;
        return event1.getStartDate().compareTo(event2.getStartDate());
    }
}
