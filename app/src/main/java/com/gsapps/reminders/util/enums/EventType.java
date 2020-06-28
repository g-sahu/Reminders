package com.gsapps.reminders.util.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public enum EventType {
    CONTACT_EVENT(1), HOLIDAY_EVENT(2), MEETING_EVENT(3), TRAVEL_EVENT(4);
    private final int code;
}
