package com.gsapps.reminders.creators;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.gsapps.reminders.models.ContactEventDTO;

public class ContactEventDTOCreator implements Creator<ContactEventDTO> {
    @Override
    public ContactEventDTO createFromParcel(Parcel in) {
        return new ContactEventDTO(in);
    }

    @Override
    public ContactEventDTO[] newArray(int size) {
        return new ContactEventDTO[size];
    }
}
