package com.gsapps.reminders.model.enums;

public enum EventType {
    CONTACT(1), HOLIDAY(2), MEETING(3), TRAVEL(4);
    private final int code;

    EventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
