package com.gsapps.reminders.creators;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.gsapps.reminders.util.enums.EventType;

import static com.gsapps.reminders.util.enums.EventType.values;

public class EventTypeCreator implements Creator<EventType> {
    @Override
    public EventType createFromParcel(Parcel in) {
        return values()[in.readInt()];
    }

    @Override
    public EventType[] newArray(int size) {
        return new EventType[size];
    }
}
