package com.gsapps.reminders.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContactEventDTO extends EventDTO {
    private String contactId;
    private String eventType;
}
