package com.gsapps.reminders.models;

import com.gsapps.reminders.util.enums.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContactEventDTO extends EventDTO {
    private String sourceEventId;

    public ContactEventDTO(EventType eventType, int icon) {
        super(eventType, icon);
    }
}
