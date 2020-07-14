package com.gsapps.reminders.util.comparators;

import com.gsapps.reminders.model.EventDTO;

import java.util.Comparator;

public class StartDateComparator implements Comparator<EventDTO>  {

    @Override
    public int compare(EventDTO lhs, EventDTO rhs) {
        return Long.compare(lhs.getStartTs(), rhs.getStartTs());
    }
}
