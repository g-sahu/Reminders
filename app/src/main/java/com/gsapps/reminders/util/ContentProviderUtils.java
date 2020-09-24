package com.gsapps.reminders.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.gsapps.reminders.util.enums.CalendarType;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import static android.provider.BaseColumns._ID;
import static android.provider.CalendarContract.Calendars;
import static android.provider.CalendarContract.Calendars.ACCOUNT_NAME;
import static android.provider.CalendarContract.Calendars.ACCOUNT_TYPE;
import static android.provider.CalendarContract.Calendars.CALENDAR_DISPLAY_NAME;
import static android.provider.CalendarContract.Calendars.OWNER_ACCOUNT;
import static android.provider.CalendarContract.Events;
import static android.provider.CalendarContract.Events.CALENDAR_ID;
import static android.provider.CalendarContract.Events.DESCRIPTION;
import static android.provider.CalendarContract.Events.DTSTART;
import static android.provider.CalendarContract.Events.TITLE;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.ListUtils.asList;
import static com.gsapps.reminders.util.ListUtils.toArray;
import static com.gsapps.reminders.util.StringUtils.join;
import static com.gsapps.reminders.util.enums.ContentProviderParam.*;
import static java.lang.String.valueOf;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ContentProviderUtils {
    private static final String LOG_TAG = ContentProviderUtils.class.getSimpleName();
    private static final String EMAIL = "simplygaurav07@gmail.com";

    public static Cursor query(@NonNull ContentResolver contentResolver, @NonNull Bundle bundle) {
        Uri uri = bundle.getParcelable(URI.name());

        if (uri == null) {
            throw new IllegalArgumentException("Content URI cannot be null");
        }

        List<String> projectionList = bundle.getStringArrayList(PROJECTION.name());
        String[] projection = projectionList == null ? null : toArray(projectionList, new String[0]);
        String selection = join(bundle.getStringArrayList(SELECTION.name()));
        List<String> selectionArgsList = bundle.getStringArrayList(SELECTION_ARGS.name());
        String[] selectionArgs = selectionArgsList == null ? null : toArray(selectionArgsList, new String[0]);
        return contentResolver.query(uri, projection, selection, selectionArgs, bundle.getString(SORT_ORDER.name()));
    }

    public static Bundle createContentProviderBundle(Parcelable uri, ArrayList<String> projection, ArrayList<String> selection, ArrayList<String> selectionArgs, String sortOrder) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(URI.name(), uri);
        bundle.putStringArrayList(PROJECTION.name(), projection);
        bundle.putStringArrayList(SELECTION.name(), selection);
        bundle.putStringArrayList(SELECTION_ARGS.name(), selectionArgs);
        bundle.putString(SORT_ORDER.name(), sortOrder);
        return bundle;
    }

    public static Bundle createCalendarBundle(CalendarType calendarType) {
        ArrayList<String> calendarsProjection = asList(_ID, CALENDAR_DISPLAY_NAME, ACCOUNT_NAME, ACCOUNT_TYPE, OWNER_ACCOUNT);
        ArrayList<String> calendarsSelection = null;
        ArrayList<String> calendarsSelectionArgs = null;

        switch (calendarType) {
            case COMPREHENSIVE:
                calendarsSelection = asList(ACCOUNT_NAME + " = ? ", ACCOUNT_TYPE + " = ?");
                calendarsSelectionArgs = asList(EMAIL, "com.google");
                break;

            case CONTACT_EVENTS:
                calendarsSelection = asList(ACCOUNT_NAME + " = ? ", ACCOUNT_TYPE + " = ?", OWNER_ACCOUNT + " = ?");
                calendarsSelectionArgs = asList(EMAIL, "com.google", ADDRESS_BOOK_CONTACTS);
                break;

            default:
                Log.e(LOG_TAG, "Unrecognised calendar type: " + calendarType);
        }

        return createContentProviderBundle(Calendars.CONTENT_URI, calendarsProjection, calendarsSelection, calendarsSelectionArgs, null);
    }

    public static Bundle createEventsBundle(long millisFrom, long millisTo) {
        ArrayList<String> eventsProjection = asList(_ID, TITLE, DESCRIPTION, DTSTART);
        ArrayList<String> eventsSelection = asList(CALENDAR_ID + " = ?", DTSTART + " >= ?");
        ArrayList<String> eventSelectionArgs = asList(valueOf(millisFrom));

        if (millisTo != 0) {
            eventsSelection.add(DTSTART + " <= ?");
            eventSelectionArgs.add(valueOf(millisTo));
        }

        return createContentProviderBundle(Events.CONTENT_URI, eventsProjection, eventsSelection, eventSelectionArgs, null);
    }

    public static boolean hasNext(Cursor cursor) {
        return cursor != null && cursor.moveToNext();
    }
}
