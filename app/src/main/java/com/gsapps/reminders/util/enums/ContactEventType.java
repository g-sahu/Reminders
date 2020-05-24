package com.gsapps.reminders.util.enums;

public enum ContactEventType {
    BIRTHDAY(1), ANNIVERSARY(2);
    private final int code;

    ContactEventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}