package com.gsapps.reminders.model;

import com.gsapps.reminders.model.enums.EventType;

public class EventFactory {
    private static EventFactory eventFactory = null;

    private EventFactory() {}

    public static EventFactory getEventFactory() {
        if(eventFactory == null) {
            synchronized (EventFactory.class) {
                if(eventFactory == null) {
                    eventFactory = new EventFactory();
                }
            }
        }

        return eventFactory;
    }

    public Event getEvent(EventType eventType) {
        switch (eventType) {
            case CONTACT:
                return new ContactEvent();

            case HOLIDAY:
                return new HolidayEvent();

            case MEETING:
                return new MeetingEvent();

            case TRAVEL:
                return new TravelEvent();

            default:
                return null;
        }
    }
}
