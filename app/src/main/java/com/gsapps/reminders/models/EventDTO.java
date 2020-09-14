package com.gsapps.reminders.models;

import com.gsapps.reminders.util.enums.EventType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static lombok.EqualsAndHashCode.Include;

@Getter @Setter @RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
public abstract class EventDTO {
    @Include protected long eventId;
    protected String title;
    protected final EventType eventType;
    protected String eventDesc;
    protected boolean isRecurring;
    protected long startTs;
    protected long endTs;
    protected long createTs;
    protected long lastUpdateTs;
    protected final int icon;
    protected final String dateFormat = "d'%s' MMM, yyyy";
}
