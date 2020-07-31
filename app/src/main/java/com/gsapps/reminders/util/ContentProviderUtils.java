package com.gsapps.reminders.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import static com.gsapps.reminders.util.ListUtils.toArray;
import static com.gsapps.reminders.util.StringUtils.join;
import static com.gsapps.reminders.util.enums.ContentProviderParam.*;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ContentProviderUtils {
    private static final String LOG_TAG = ContentProviderUtils.class.getSimpleName();

    public static Cursor query(@NonNull ContentResolver contentResolver, @NonNull Bundle bundle) {
        Uri uri = bundle.getParcelable(URI.name());

        if (uri == null) {
            throw new IllegalArgumentException("Content URI cannot be null");
        }

        String[] projection = toArray(bundle.getStringArrayList(PROJECTION.name()), new String[0]);
        String selection = join(bundle.getStringArrayList(SELECTION.name()));
        String[] selectionArgs = toArray(bundle.getStringArrayList(SELECTION_ARGS.name()), new String[0]);
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

    public static boolean hasNext(Cursor cursor) {
        return cursor != null && cursor.moveToNext();
    }
}
