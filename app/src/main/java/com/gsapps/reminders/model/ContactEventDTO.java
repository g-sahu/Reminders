package com.gsapps.reminders.model;

import android.os.Parcel;

import com.gsapps.reminders.creators.ContactEventDTOCreator;
import com.gsapps.reminders.util.enums.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ContactEventDTO extends EventDTO {
    public static final Creator<ContactEventDTO> CREATOR = new ContactEventDTOCreator();
    private String sourceEventId;

    // TODO: 13-07-2020 Remove ContactEventType
    //private ContactEventType contactEventType;

    public ContactEventDTO(EventType eventType, int icon) {
        super(eventType, icon);
    }

    public ContactEventDTO(Parcel in) {
        super(in);
        sourceEventId = in.readString();
    }

    @Override
    public int describeContents() {
        super.describeContents();
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(sourceEventId);
    }
}
