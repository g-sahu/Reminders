package com.gsapps.reminders.util.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public enum ContactEventType {
    BIRTHDAY(1), ANNIVERSARY(2);
    private final int code;
}
