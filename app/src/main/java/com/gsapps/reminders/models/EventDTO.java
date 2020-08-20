package com.gsapps.reminders.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.gsapps.reminders.util.enums.EventType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static lombok.EqualsAndHashCode.Include;

@Getter @Setter @RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("AbstractClassWithoutAbstractMethods")
public abstract class EventDTO implements Parcelable {
    @Include protected long eventId;
    protected String title;
    protected final EventType eventType;
    protected String eventDesc;
    //protected boolean isRecurring;
    protected long startTs;
    protected long endTs;
    protected long createTs;
    protected long lastUpdateTs;
    protected final int icon;

    protected EventDTO(Parcel in) {
        eventId = in.readInt();
        title = in.readString();
        eventType = in.readParcelable(EventType.class.getClassLoader());
        eventDesc = in.readString();
        // TODO: 13-07-2020 Convert boolean to int to serialise this field
        //isRecurring = in.readBoolean();
        startTs = in.readLong();
        endTs = in.readLong();
        createTs = in.readLong();
        lastUpdateTs = in.readLong();
        icon = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(eventId);
        dest.writeString(title);
        dest.writeParcelable(eventType, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeString(eventDesc);
        // TODO: 13-07-2020 Convert boolean to int to serialise this field
        //dest.writeBoolean(isRecurring);
        dest.writeLong(startTs);
        dest.writeLong(endTs);
        dest.writeLong(createTs);
        dest.writeLong(lastUpdateTs);
        dest.writeInt(icon);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
