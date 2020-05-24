package com.gsapps.reminders.util.comparators;

import com.gsapps.reminders.model.EventDTO;

import java.util.Comparator;

public class StartDateComparator implements Comparator<Object>  {

    @Override
    public int compare(Object lhs, Object rhs) {
        if(lhs instanceof EventDTO && rhs instanceof EventDTO) {
            EventDTO eventDTO1 = (EventDTO) lhs;
            EventDTO eventDTO2 = (EventDTO) rhs;
            return eventDTO1.getStartTs().compareTo(eventDTO2.getStartTs());
        } else {
            throw new RuntimeException("Objects are not mutually comparable.");
        }
    }
}
