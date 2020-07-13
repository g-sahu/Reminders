package com.gsapps.reminders.factories;

import android.util.Log;

import com.gsapps.reminders.model.ContactEventDTO;
import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.model.HolidayEventDTO;
import com.gsapps.reminders.util.enums.EventType;

import lombok.NoArgsConstructor;

import static com.gsapps.reminders.R.drawable.holiday;
import static com.gsapps.reminders.R.drawable.outline_cake_black_18;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_IN;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.HOLIDAY_US;
import static com.gsapps.reminders.util.enums.EventType.CONTACT_EVENT;
import static com.gsapps.reminders.util.enums.EventType.HOLIDAY_EVENT;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class EventDTOFactory {
    private static final String LOG_TAG = EventDTOFactory.class.getSimpleName();

    public static EventDTO createEventDTO(EventType eventType) {
        switch (eventType) {
            case CONTACT_EVENT:
                return new ContactEventDTO(eventType, outline_cake_black_18);

            case HOLIDAY_EVENT:
                return new HolidayEventDTO(eventType, holiday);

            case MEETING_EVENT:
                // TODO: 13-07-2020 Implement Parcelable in MeetingEventDTO to enable this commented code
                //return new MeetingEventDTO(eventType, 0);
                return null;

            case TRAVEL_EVENT:
                // TODO: 13-07-2020 Implement Parcelable in TravelEventDTO to enable this commented code
                //return new TravelEventDTO(eventType, 0);
                return null;
        }

        return null;
    }

    public static EventDTO createEventDTO(String ownerAccount) {
        EventDTO eventDTO = null;

        switch (ownerAccount) {
            case ADDRESS_BOOK_CONTACTS:
                eventDTO = createEventDTO(CONTACT_EVENT);
                break;

            case HOLIDAY_IN:
            case HOLIDAY_US:
                eventDTO = createEventDTO(HOLIDAY_EVENT);
                break;

            default:
                Log.e(LOG_TAG, "Unrecognised owner account: " + ownerAccount);
        }

        return eventDTO;
    }
}
