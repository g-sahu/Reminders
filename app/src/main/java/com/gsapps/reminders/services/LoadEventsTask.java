package com.gsapps.reminders.services;

import android.app.Application;
import android.os.AsyncTask;

import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoadEventsTask extends AsyncTask<CalendarType, Void, List<EventDTO>> {
    private static final String LOG_TAG = LoadEventsTask.class.getSimpleName();
    private final Application application;

    @Override
    protected List<EventDTO> doInBackground(CalendarType... calendarType) {
        return new RemindersService(application).getEvents(calendarType[0]);
    }
}
