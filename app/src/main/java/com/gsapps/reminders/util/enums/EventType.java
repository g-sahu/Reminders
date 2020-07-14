package com.gsapps.reminders.util.enums;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public enum EventType implements Parcelable {
    CONTACT_EVENT(1), HOLIDAY_EVENT(2), MEETING_EVENT(3), TRAVEL_EVENT(4);
    private final int code;
    public static final Creator<EventType> CREATOR = new EventTypeCreator();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {dest.writeInt(code);}

}
