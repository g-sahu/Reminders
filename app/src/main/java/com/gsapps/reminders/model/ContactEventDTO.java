package com.gsapps.reminders.model;

import com.gsapps.reminders.util.enums.ContactEventType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContactEventDTO extends EventDTO {
    private String sourceEventId;
    private ContactEventType contactEventType;
}
