package com.gsapps.reminders.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.widget.Toast;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import com.microsoft.graph.extensions.DateTimeTimeZone;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.gsapps.reminders.R.string.key_connect_with_outlook;
import static java.util.Calendar.*;
import static java.util.TimeZone.getDefault;
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

    public static String getDateString(Calendar calendar, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format); // TODO: 09-04-2019 Convert start date to local date format
        return dateFormat.format(calendar.getTime());
    }

    public static boolean isOutlookConnected(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(context.getString(key_connect_with_outlook), false);
    }

    public static Calendar getTodaysCalendar() {
        Calendar calendar = getInstance();
        calendar.set(HOUR_OF_DAY, 0);
        calendar.set(MINUTE, 0);
        calendar.set(SECOND, 0);
        calendar.set(MILLISECOND, 0);
        return calendar;
    }

    private static String getTodaysDateString(String format) {
         return getDateString(getTodaysCalendar(), format);
    }

    public static List<Option> getOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new HeaderOption("Prefer", "outlook.timezone=\"" + getDefault().getID() + "\""));
        options.add(new QueryOption("select", "subject,bodyPreview,start,end,location"));
        options.add(new QueryOption("filter", "start/dateTime ge '" + getTodaysDateString("yyyy-MM-dd HH:mm") + "'"));
        options.add(new QueryOption("orderby", "start/dateTime"));
        return options;
    }

    /*public static Event getEvent(String eventType) {
        Event event = null;

        if(eventType.equals(ANNIVERSARY.toString())) {
            event = getEventFactory().getEvent(ANNIVERSARY);
        } else if (eventType.equals(BIRTHDAY.toString()) || eventType.equals("SELF")){ // TODO: 15-04-2019 Check if Event Type 'Self' is for events other than own birthday
            event = getEventFactory().getEvent(BIRTHDAY);
        }

        return event;
    }*/
}
