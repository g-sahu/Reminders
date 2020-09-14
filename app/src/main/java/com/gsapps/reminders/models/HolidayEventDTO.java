package com.gsapps.reminders.models;

import com.gsapps.reminders.util.enums.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HolidayEventDTO extends EventDTO {
    private String calendar;

    public HolidayEventDTO(EventType eventType, int icon) {
        super(eventType, icon);
    }
}
