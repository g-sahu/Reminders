package com.gsapps.reminders.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gsapps.reminders.models.EventDTO;
import com.gsapps.reminders.services.LoadEventsTask;
import com.gsapps.reminders.services.LoadEventsTask.Params;
import com.gsapps.reminders.util.comparators.StartDateComparator;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.gsapps.reminders.util.CalendarUtils.getStartOfDayMillis;
import static com.gsapps.reminders.util.enums.CalendarType.COMPREHENSIVE;
import static com.gsapps.reminders.util.enums.CalendarType.CONTACT_EVENTS;
import static java.util.Collections.emptyList;
import static java.util.Collections.sort;

public class HomeActivityViewModel extends AndroidViewModel {
    private static final String LOG_TAG = HomeActivityViewModel.class.getSimpleName();
    private final AsyncTask<Params, Void, List<EventDTO>> loadEventsTask;
    private MutableLiveData<List<EventDTO>> myCalendarEventsLiveData, contactEventsLiveData;

    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
        loadEventsTask = new LoadEventsTask(application);
    }

    private List<EventDTO> getEvents(CalendarType calendarType) {
        try {
            List<EventDTO> events = loadEventsTask.execute(new Params(calendarType, getStartOfDayMillis(), 0)).get();
            sort(events, new StartDateComparator());
            return events;
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOG_TAG, "Error while fetching events: " + e.getMessage());
            return emptyList();
        }
    }

    public MutableLiveData<List<EventDTO>> getMyCalendarEvents() {
        if (myCalendarEventsLiveData != null) {
            return myCalendarEventsLiveData;
        }

        myCalendarEventsLiveData = new MutableLiveData<>();
        myCalendarEventsLiveData.postValue(getEvents(COMPREHENSIVE));
        return myCalendarEventsLiveData;
    }

    public MutableLiveData<List<EventDTO>> getContactEvents() {
        if (contactEventsLiveData != null) {
            return contactEventsLiveData;
        }

        contactEventsLiveData = new MutableLiveData<>();
        contactEventsLiveData.postValue(getEvents(CONTACT_EVENTS));
        return contactEventsLiveData;
    }
}
