package com.gsapps.reminders.services;

import org.junit.After;
import org.junit.Before;

public class RemindersServiceUnitTest {
    //private RemindersService remindersService = new RemindersService();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

   /* @Test
    public void getEventDTOs_AllCalendar() {
        String[] PROJECTION_CALENDARS = {_ID, OWNER_ACCOUNT};
        String[] PROJECTION_EVENTS = {_ID, TITLE, DESCRIPTION, DTSTART};
        String calendarsSelection = ACCOUNT_NAME + " = ? AND " + ACCOUNT_TYPE + " = ?";
        String[] calendarsSelectionArgs = new String[]{"simplygaurav07@gmail.com", "com.google"};
        String eventsSelection = CALENDAR_ID + " = ? AND " + DTSTART + " >= ?";
        Bundle calendarsBundle = createContentProviderBundle(Calendars.CONTENT_URI, PROJECTION_CALENDARS, calendarsSelection, calendarsSelectionArgs, null);
        Bundle eventsBundle = createContentProviderBundle(Events.CONTENT_URI, PROJECTION_EVENTS, eventsSelection, null, null);
        //List<EventDTO> actualEventDTOs = remindersService.getEventDTOs(context, calendarsBundle, eventsBundle);

        List<EventDTO> expectedEventDTOs = new ArrayList<>();
        EventDTO eventDTO = EventDTOFactory.createEventDTO(CONTACT_EVENT);
    }*/
}
