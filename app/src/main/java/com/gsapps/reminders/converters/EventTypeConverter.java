package com.gsapps.reminders.converters;

import androidx.room.TypeConverter;
import com.gsapps.reminders.util.enums.EventType;

import static com.gsapps.reminders.util.enums.EventType.CONTACT_EVENT;
import static com.gsapps.reminders.util.enums.EventType.HOLIDAY_EVENT;
import static com.gsapps.reminders.util.enums.EventType.MEETING_EVENT;
import static com.gsapps.reminders.util.enums.EventType.TRAVEL_EVENT;

public class EventTypeConverter {

    @TypeConverter
    public static EventType toEventType(int code) {

        if (code == CONTACT_EVENT.getCode()) {
            return CONTACT_EVENT;
        } else if (code == HOLIDAY_EVENT.getCode()) {
            return HOLIDAY_EVENT;
        } else if (code == MEETING_EVENT.getCode()) {
            return MEETING_EVENT;
        } else if (code == TRAVEL_EVENT.getCode()) {
            return TRAVEL_EVENT;
        } else {
            throw new IllegalArgumentException("Could not recognize event type code: " + code);
        }
    }

    @TypeConverter
    public static int fromEventType(EventType eventType) {
        return eventType.getCode();
    }
}
