package com.gsapps.reminders.model;

import android.os.Parcel;

import com.gsapps.reminders.creators.HolidayEventDTOCreator;
import com.gsapps.reminders.util.enums.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HolidayEventDTO extends EventDTO {
    public static final Creator<HolidayEventDTO> CREATOR = new HolidayEventDTOCreator();
    private String calendar;

    public HolidayEventDTO(EventType eventType, int icon) {
        super(eventType, icon);
    }

    public HolidayEventDTO(Parcel in) {
        super(in);
        calendar = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(calendar);
    }
}
