package com.gsapps.reminders.model;

import com.gsapps.reminders.util.enums.EventType;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static lombok.EqualsAndHashCode.Include;

@Getter @Setter @RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class EventDTO {
    @Include protected long eventId;
    protected String title;
    protected final EventType eventType;
    protected String eventDesc;
    protected boolean isRecurring;
    protected LocalDateTime startTs;
    protected LocalDateTime endTs;
    protected LocalDateTime createTs;
    protected LocalDateTime lastUpdateTs;
    protected final int icon;
}
