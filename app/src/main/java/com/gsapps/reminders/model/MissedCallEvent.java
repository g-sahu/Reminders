package com.gsapps.reminders.model;

import java.sql.Timestamp;

public class MissedCallEvent extends Event {
    private String callerName;
    private long phoneNumber;
    private Timestamp timestamp;
}
