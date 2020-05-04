package com.gsapps.reminders.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TravelEventDTO extends EventDTO {
    private String source;
    private String destination;
}
