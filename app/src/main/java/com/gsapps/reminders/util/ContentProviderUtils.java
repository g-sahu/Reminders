package com.gsapps.reminders.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ContentProviderUtils {
    private static final String LOG_TAG = ContentProviderUtils.class.getSimpleName();
    private static final String URI = "URI";
    private static final String PROJECTION = "PROJECTION";
    private static final String SELECTION = "SELECTION";
    public static final String SELECTION_ARGS = "SELECTION_ARGS";
    private static final String SORT_ORDER = "SORT_ORDER";

    public static Cursor query(ContentResolver contentResolver, Bundle bundle) {
        return contentResolver.query(bundle.getParcelable(URI), bundle.getStringArray(PROJECTION), bundle.getString(SELECTION), bundle.getStringArray(SELECTION_ARGS), bundle.getString(SORT_ORDER));
    }

    public static Bundle createContentProviderBundle(Parcelable uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(URI, uri);
        bundle.putStringArray(PROJECTION, projection);
        bundle.putString(SELECTION, selection);
        bundle.putStringArray(SELECTION_ARGS, selectionArgs);
        bundle.putString(SORT_ORDER, sortOrder);
        return bundle;
    }
}
