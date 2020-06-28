package com.gsapps.reminders.model;

import com.gsapps.reminders.util.enums.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TravelEventDTO extends EventDTO {
    private String source;
    private String destination;

    public TravelEventDTO(EventType eventType, int icon) {
        super(eventType, icon);
    }
}
