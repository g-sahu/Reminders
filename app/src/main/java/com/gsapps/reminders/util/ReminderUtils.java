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
import com.microsoft.graph.extensions.DateTimeTimeZone;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.gsapps.reminders.R.string.key_connect_with_outlook;
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

    public static Calendar getCalendar(DateTimeTimeZone dateTimeTimeZone) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS";
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = dateFormat.parse(dateTimeTimeZone.dateTime);
            calendar.setTimeInMillis(date.getTime());
            calendar.setTimeZone(getTimeZone(dateTimeTimeZone.timeZone));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String getDateString(Calendar calendar) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm a"); // TODO: 09-04-2019 Convert start date to local date format
        return dateFormat.format(calendar.getTime());
    }

    public static boolean isOutlookConnected(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(context.getString(key_connect_with_outlook), false);
    }
}
