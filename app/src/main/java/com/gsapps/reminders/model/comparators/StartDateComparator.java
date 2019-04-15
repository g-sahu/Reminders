package com.gsapps.reminders.model.comparators;

import com.gsapps.reminders.model.Event;

import java.util.Comparator;

public class StartDateComparator implements Comparator<Object>  {

    @Override
    public int compare(Object lhs, Object rhs) {
        if(lhs instanceof Event && rhs instanceof Event) {
            Event event1 = (Event) lhs;
            Event event2 = (Event) rhs;
            return event1.getStartDate().compareTo(event2.getStartDate());
        } else {
            throw new RuntimeException("Objects are not mutually comparable.");
        }
    }
}
