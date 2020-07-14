package com.gsapps.reminders.util.enums;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.gsapps.reminders.model.HolidayEventDTO;

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
