package com.gsapps.reminders.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static lombok.EqualsAndHashCode.Include;

@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CalendarDTO {
    @Include private int calendarID;
    private String name;
    private String accountName;
    private String accountType;
    private String ownerAccount;

}
