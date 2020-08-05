package com.gsapps.reminders.viewmodels;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.gsapps.reminders.model.EventDTO;
import com.gsapps.reminders.services.LoadEventsTask;
import com.gsapps.reminders.util.comparators.StartDateComparator;
import com.gsapps.reminders.util.enums.CalendarType;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.gsapps.reminders.util.enums.CalendarType.COMPREHENSIVE;
import static java.util.Collections.sort;

public class EventViewModel extends AndroidViewModel {
    private static final String LOG_TAG = EventViewModel.class.getSimpleName();
    private final AsyncTask<CalendarType, Void, List<EventDTO>> loadEventsTask;
    private MutableLiveData<List<EventDTO>> eventsLiveData;

    public EventViewModel(@NonNull Application application) {
        super(application);
        loadEventsTask = new LoadEventsTask(application);
    }

    public MutableLiveData<List<EventDTO>> getEvents() {
        if (eventsLiveData == null) {
            eventsLiveData = new MutableLiveData<>();

            try {
                List<EventDTO> events = loadEventsTask.execute(COMPREHENSIVE).get();
                sort(events, new StartDateComparator());
                eventsLiveData.postValue(events);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        return eventsLiveData;
    }
}
