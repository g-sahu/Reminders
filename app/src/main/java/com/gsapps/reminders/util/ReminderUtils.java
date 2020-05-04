package com.gsapps.reminders.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.Calendar;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.google.api.client.extensions.android.http.AndroidHttp.newCompatibleTransport;
import static com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance;
import static com.gsapps.reminders.R.string.key_connect_with_outlook;
import static java.util.TimeZone.getDefault;

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

    public static boolean isOutlookConnected(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(context.getString(key_connect_with_outlook), false);
    }

    public static List<Option> getOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new HeaderOption("Prefer", "outlook.timezone=\"" + getDefault().getID() + "\""));
        options.add(new QueryOption("select", "subject,bodyPreview,start,end,location"));
        options.add(new QueryOption("filter", "start/dateTime ge '" + CalendarUtils.getTodaysDateString("yyyy-MM-dd HH:mm") + "'"));
        options.add(new QueryOption("orderby", "start/dateTime"));
        return options;
    }

    public static boolean isNotNullOrEmpty(String str) {
        return (str != null) && !(str.trim().isEmpty());
    }

    /*public static EventDTO getEvent(String eventType) {
        EventDTO event = null;

        if(eventType.equals(ANNIVERSARY.toString())) {
            event = getEventDTOFactory().getEvent(ANNIVERSARY);
        } else if (eventType.equals(BIRTHDAY.toString()) || eventType.equals("SELF")){ // TODO: 15-04-2019 Check if EventDTO Type 'Self' is for events other than own birthday
            event = getEventDTOFactory().getEvent(BIRTHDAY);
        }

        return event;
    }*/

    public static Calendar getCalendar(GoogleAccountCredential credential, String appName) {
        return new Calendar.Builder(newCompatibleTransport(), getDefaultInstance(), credential)
                .setApplicationName(appName)
                .build();
    }
}
