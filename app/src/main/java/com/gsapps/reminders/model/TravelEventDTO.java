package com.gsapps.reminders.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
// TODO: 13-07-2020 Implement Parcelable to enable this commented code
public class TravelEventDTO {//extends EventDTO {
    private String source;
    private String destination;

    /*public TravelEventDTO(EventType eventType, int icon) {
        super(eventType, icon);
    }*/
}
