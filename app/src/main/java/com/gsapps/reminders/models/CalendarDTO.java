package com.gsapps.reminders.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static lombok.EqualsAndHashCode.Include;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CalendarDTO {
    @Include private final int calendarID;
    private String calendarName;
    private String accountName;
    private String accountType;
    private String ownerAccount;

}
