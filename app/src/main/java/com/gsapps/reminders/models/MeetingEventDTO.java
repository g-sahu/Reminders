package com.gsapps.reminders.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
// TODO: 13-07-2020 Implement Parcelable to enable this commented code
public class MeetingEventDTO {//extends EventDTO {
    private String subject;
    private String organiser;
    private String location;

    /*public MeetingEventDTO(EventType eventType, int icon) {
        super(eventType, icon);
    }*/
}
