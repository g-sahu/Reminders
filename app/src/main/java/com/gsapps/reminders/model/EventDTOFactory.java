package com.gsapps.reminders.model;

import com.gsapps.reminders.model.enums.EventType;

public class EventDTOFactory {
    private static EventDTOFactory eventDTOFactory = null;

    private EventDTOFactory() {}

    public static EventDTOFactory getEventDTOFactory() {
        if(eventDTOFactory == null) {
            synchronized (EventDTOFactory.class) {
                if(eventDTOFactory == null) {
                    eventDTOFactory = new EventDTOFactory();
                }
            }
        }

        return eventDTOFactory;
    }

    public EventDTO getEvent(EventType eventType) {
        switch (eventType) {
            case CONTACT:
                return new ContactEventDTO();

            case HOLIDAY:
                return new HolidayEventDTO();

            case MEETING:
                return new MeetingEventDTO();

            case TRAVEL:
                return new TravelEventDTO();

            default:
                return null;
        }
    }
}
