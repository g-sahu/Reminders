package com.gsapps.reminders.converters;

import androidx.room.TypeConverter;
import com.gsapps.reminders.model.enums.ContactEventType;

import static com.gsapps.reminders.model.enums.ContactEventType.ANNIVERSARY;
import static com.gsapps.reminders.model.enums.ContactEventType.BIRTHDAY;

public class ContactEventTypeConverter {

    @TypeConverter
    public static ContactEventType toContactEventType(int code) {
        if (code == BIRTHDAY.getCode()) {
            return BIRTHDAY;
        } else if (code == ANNIVERSARY.getCode()) {
            return ANNIVERSARY;
        } else {
            throw new IllegalArgumentException("Could not recognize contact event type code: " + code);
        }
    }

    @TypeConverter
    public static int fromContactEventType(ContactEventType eventType) {
        return eventType.getCode();
    }
}
