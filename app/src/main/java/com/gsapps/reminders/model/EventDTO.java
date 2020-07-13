package com.gsapps.reminders.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gsapps.reminders.util.enums.EventType;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static lombok.EqualsAndHashCode.Include;

@Getter @Setter @RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class EventDTO implements Parcelable {
    @Include protected long eventId;
    protected String title;
    protected final EventType eventType;
    protected String eventDesc;
    //protected boolean isRecurring;

    protected LocalDateTime startTs;
    protected LocalDateTime endTs;
    protected LocalDateTime createTs;
    protected LocalDateTime lastUpdateTs;
    protected final int icon;

    protected EventDTO(Parcel in) {
        eventId = in.readInt();
        title = in.readString();
        eventType = in.readParcelable(EventType.class.getClassLoader());
        eventDesc = in.readString();
        // TODO: 13-07-2020 Convert boolean to int to serialise this field
        //isRecurring = in.readBoolean();

        // TODO: 13-07-2020 Find a way to serialize these fields
        /*startTs = (LocalDateTime) in.readValue(LocalDateTime.class.getClassLoader());
        endTs = (LocalDateTime) in.readValue(LocalDateTime.class.getClassLoader());
        createTs = (LocalDateTime) in.readValue(LocalDateTime.class.getClassLoader());
        lastUpdateTs = (LocalDateTime) in.readValue(LocalDateTime.class.getClassLoader());*/
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
        dest.writeValue(startTs);
        dest.writeValue(endTs);
        dest.writeValue(createTs);
        dest.writeValue(lastUpdateTs);
        dest.writeInt(icon);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
