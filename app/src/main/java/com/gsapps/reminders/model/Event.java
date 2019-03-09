package com.gsapps.reminders.model;

//@Getter @Setter
public class Event {
    protected String name;
    protected String desc;
    protected Frequency frequency;
    protected boolean isRecurring;
    protected byte[] icon;

    public enum Frequency {
        YEARLY, HALF_YEARLY, QUARTERLY, MONTHLY, FORTNIGHTLY, WEEKLY, DAILY, ONCE
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
}
