package com.gsapps.reminders.model;

import com.gsapps.reminders.util.enums.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MeetingEventDTO extends EventDTO {
    private String subject;
    private String organiser;
    private String location;

    public MeetingEventDTO(EventType eventType, int icon) {
        super(eventType, icon);
    }
}
