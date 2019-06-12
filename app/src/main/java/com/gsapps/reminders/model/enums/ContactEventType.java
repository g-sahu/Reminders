package com.gsapps.reminders.model.enums;

public enum ContactEventType {
    BIRTHDAY(1), ANNIVERSARY(2);
    private int code;

    ContactEventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
