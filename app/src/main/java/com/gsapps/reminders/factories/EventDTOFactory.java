package com.gsapps.reminders.factories;

import android.util.Log;

import com.gsapps.reminders.models.ContactEventDTO;
import com.gsapps.reminders.models.EventDTO;
import com.gsapps.reminders.models.HolidayEventDTO;
import com.gsapps.reminders.models.MeetingEventDTO;
import com.gsapps.reminders.util.enums.EventType;

import lombok.NoArgsConstructor;

import static com.gsapps.reminders.R.drawable.holiday;
import static com.gsapps.reminders.R.drawable.outline_cake_black_18;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_IN;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_US;
import static com.gsapps.reminders.util.enums.EventType.CONTACT_EVENT;
import static com.gsapps.reminders.util.enums.EventType.HOLIDAY_EVENT;
import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class EventDTOFactory {
    private static final String LOG_TAG = EventDTOFactory.class.getSimpleName();
    private static final String MSG_IAE = "Unrecognised %s: ";

    public static EventDTO createEventDTO(EventType eventType) {
        String msg = format(MSG_IAE, "eventType") + eventType;

        switch (eventType) {
            case CONTACT_EVENT:
                return new ContactEventDTO(eventType, outline_cake_black_18);

            case HOLIDAY_EVENT:
                return new HolidayEventDTO(eventType, holiday);

            case MEETING_EVENT:
                return new MeetingEventDTO(eventType, 0);

            case TRAVEL_EVENT:
                // TODO: 13-07-2020 Implement Parcelable in TravelEventDTO to enable this commented code
                //return new TravelEventDTO(eventType, 0);
                Log.e(LOG_TAG, msg);
                throw new IllegalArgumentException(msg);

            default:
                Log.e(LOG_TAG, msg);
                throw new IllegalArgumentException(msg);
        }
    }

    public static EventDTO createEventDTO(String ownerAccount) {
        switch (ownerAccount) {
            case ADDRESS_BOOK_CONTACTS:
                return createEventDTO(CONTACT_EVENT);

            case HOLIDAY_IN:
            case HOLIDAY_US:
                return createEventDTO(HOLIDAY_EVENT);

            default:
                String msg = format(MSG_IAE, "ownerAccount") + ownerAccount;
                Log.e(LOG_TAG, msg);
                throw new IllegalArgumentException(msg);
        }
    }
}
