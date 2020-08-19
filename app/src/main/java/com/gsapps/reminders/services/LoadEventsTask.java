package com.gsapps.reminders.services;

import android.app.Application;
import android.os.AsyncTask;

import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.services.LoadEventsTask.Params;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import static com.gsapps.reminders.util.ContentProviderUtils.createEventsBundle;

@RequiredArgsConstructor
public class LoadEventsTask extends AsyncTask<Params, Void, List<EventDTO>> {
    private static final String LOG_TAG = LoadEventsTask.class.getSimpleName();
    private final Application application;

    @Override
    protected List<EventDTO> doInBackground(Params... params) {
        Params param = params[0];
        return new RemindersService(application).getEvents(param.calendarType, createEventsBundle(param.millisFrom, param.millisTo));
    }

    @Data
    public static class Params {
        private final CalendarType calendarType;
        private final long millisFrom;
        private final long millisTo;
    }
}
