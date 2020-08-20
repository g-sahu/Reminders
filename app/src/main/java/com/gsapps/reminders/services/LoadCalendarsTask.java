package com.gsapps.reminders.services;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;

import com.gsapps.reminders.models.CalendarDTO;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoadCalendarsTask extends AsyncTask<Bundle, Void, List<CalendarDTO>> {
    private static final String LOG_TAG = LoadCalendarsTask.class.getSimpleName();
    private final Application application;

    @Override
    protected List<CalendarDTO> doInBackground(Bundle... bundles) {
        return new RemindersService(application).getCalendars(bundles[0]);
    }
}
