package com.gsapps.reminders.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.util.Calendar.getInstance;
import static java.util.TimeZone.getTimeZone;

public class ReminderUtils {
    private static Toast toast;

    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        return checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }

    public static List<String> checkPermissions(@NonNull Context context, @NonNull List<String> permissions) {
        List<String> notGrantedPermissions = new ArrayList<>(permissions.size());

        for(String permission: permissions) {
            if(!hasPermission(context, permission)) {
                notGrantedPermissions.add(permission);
            }
        }

        return notGrantedPermissions;
    }

    @SuppressLint("Recycle")
    public static Cursor[] getContentFromProvider(Context context, Uri[] uris, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursors[] = new Cursor[uris.length];
        ContentResolver contentResolver = context.getContentResolver();

        for (int i=0; i<uris.length; i++) {
            cursors[i] = contentResolver.query(uris[i], projection, selection, selectionArgs, sortOrder);
        }

        return cursors;
    }

    public static void showToastMessage(Context context, String message) {
        if(toast != null) {
            toast.cancel();
        }

        toast = makeText(context, message, LENGTH_SHORT);
        toast.show();
    }

    public static Calendar getCalendar(EventDateTime eventDateTime, String timezone) {
        DateTime dateTime = (eventDateTime.getDate() != null) ? eventDateTime.getDate() : eventDateTime.getDateTime();
        Calendar calendar = getInstance(getTimeZone(timezone));
        calendar.setTimeInMillis(dateTime.getValue());
        return calendar;
    }
}
