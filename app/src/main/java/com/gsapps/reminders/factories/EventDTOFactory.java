package com.gsapps.reminders.factories;

import com.gsapps.reminders.model.ContactEventDTO;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.model.HolidayEventDTO;
import com.gsapps.reminders.model.MeetingEventDTO;
import com.gsapps.reminders.model.TravelEventDTO;
import com.gsapps.reminders.util.enums.EventType;
import lombok.NoArgsConstructor;

import static com.gsapps.reminders.R.drawable.holiday;
import static com.gsapps.reminders.R.drawable.outline_cake_black_18;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class EventDTOFactory {
    private static EventDTOFactory eventDTOFactory = null;

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

    public EventDTO createEventDTO(EventType eventType) {
        switch (eventType) {
            case CONTACT:
                return new ContactEventDTO(outline_cake_black_18);

            case HOLIDAY:
                return new HolidayEventDTO(holiday);

            case MEETING:
                return new MeetingEventDTO(0);

            case TRAVEL:
                return new TravelEventDTO(0);

            default:
                return null;
        }
    }
}
