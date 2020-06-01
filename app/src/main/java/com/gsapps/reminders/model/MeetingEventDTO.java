package com.gsapps.reminders.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MeetingEventDTO extends EventDTO {
    private String subject;
    private String organiser;
    private String location;

    public MeetingEventDTO(int icon) {
        super(icon);
    }
}
