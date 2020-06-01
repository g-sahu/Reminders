package com.gsapps.reminders.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HolidayEventDTO extends EventDTO {
    private String calendar;

    public HolidayEventDTO(int icon) {
        super(icon);
    }
}
