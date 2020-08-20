package com.gsapps.reminders.creators;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.gsapps.reminders.models.HolidayEventDTO;

public class HolidayEventDTOCreator implements Creator<HolidayEventDTO> {
    @Override
    public HolidayEventDTO createFromParcel(Parcel in) {
        return new HolidayEventDTO(in);
    }

    @Override
    public HolidayEventDTO[] newArray(int size) {
        return new HolidayEventDTO[size];
    }
}
