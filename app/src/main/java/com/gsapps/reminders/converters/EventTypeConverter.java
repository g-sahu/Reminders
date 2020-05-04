package com.gsapps.reminders.converters;

import androidx.room.TypeConverter;
import com.gsapps.reminders.model.enums.EventType;

import static com.gsapps.reminders.model.enums.EventType.CONTACT;
import static com.gsapps.reminders.model.enums.EventType.HOLIDAY;
import static com.gsapps.reminders.model.enums.EventType.MEETING;
import static com.gsapps.reminders.model.enums.EventType.TRAVEL;

public class EventTypeConverter {

    @TypeConverter
    public static EventType toEventType(int code) {

        if (code == CONTACT.getCode()) {
            return CONTACT;
        } else if (code == HOLIDAY.getCode()) {
            return HOLIDAY;
        } else if (code == MEETING.getCode()) {
            return MEETING;
        } else if (code == TRAVEL.getCode()) {
            return TRAVEL;
        } else {
            throw new IllegalArgumentException("Could not recognize event type code: " + code);
        }
    }

    @TypeConverter
    public static int fromEventType(EventType eventType) {
        return eventType.getCode();
    }
}
