package com.gsapps.reminders.model;

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
            case ANNIVERSARY:
                return new AnniversaryEvent();

            case BIRTHDAY:
                return new BirthdayEvent();

            case HOLIDAY:
                return new HolidayEvent();

            case MEETING:
                return new MeetingEvent();

            case MISSED_CALL:
                return new MissedCallEvent();

            case PAYMENT:
                return new PaymentEvent();

            case TRAVEL:
                return new TravelEvent();

            default:
                return null;
        }
    }
}
