package com.gsapps.reminders.model;

import com.gsapps.reminders.util.enums.EventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

import static lombok.EqualsAndHashCode.Include;

@SuppressWarnings("UseOfObsoleteDateTimeApi")
@Getter @Setter @RequiredArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class EventDTO {
    @Include
    protected long eventId;
    protected String title;
    protected EventType eventType;
    protected String eventDesc;
    protected boolean isRecurring;
    protected Calendar startTs;
    protected Calendar endTs;
    protected Calendar createTs;
    protected Calendar lastUpdateTs;
    protected final int icon;
}
