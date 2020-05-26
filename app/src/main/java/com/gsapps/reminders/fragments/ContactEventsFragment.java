package com.gsapps.reminders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.gsapps.reminders.services.LoadCalendarTask;

import static android.Manifest.permission.READ_CALENDAR;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.CalendarContract.Calendars.ACCOUNT_NAME;
import static android.provider.CalendarContract.Calendars.ACCOUNT_TYPE;
import static android.provider.CalendarContract.Calendars.OWNER_ACCOUNT;
import static com.gsapps.reminders.R.id.contact_events_view;
import static com.gsapps.reminders.R.layout.fragment_contact_events;
import static com.gsapps.reminders.util.Constants.GoogleCalendarOwner.ADDRESS_BOOK_CONTACTS;
import static com.gsapps.reminders.util.Constants.Key.CALENDAR_SELECTION;
import static com.gsapps.reminders.util.Constants.Key.CALENDAR_SELECTION_ARGS;
import static com.gsapps.reminders.util.Constants.RequestCode;
import static com.gsapps.reminders.util.ReminderUtils.hasPermission;

public class ContactEventsFragment extends Fragment {
    private static final String LOG_TAG = ContactEventsFragment.class.getSimpleName();
    private Context context;
    private final Bundle bundle = new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        String calendarsSelection = ACCOUNT_NAME + " = ? AND " + ACCOUNT_TYPE + " = ? AND " + OWNER_ACCOUNT + " = ?";
        String[] calendarsSelectionArgs = {"simplygaurav07@gmail.com", "com.google", ADDRESS_BOOK_CONTACTS};
        bundle.putString(CALENDAR_SELECTION, calendarsSelection);
        bundle.putStringArray(CALENDAR_SELECTION_ARGS, calendarsSelectionArgs);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(hasPermission(context, READ_CALENDAR)) {
            new LoadCalendarTask(context, contact_events_view).execute(bundle);
        } else {
            requestPermissions(new String[]{READ_CALENDAR}, RequestCode.READ_CALENDAR);
        }

        return inflater.inflate(fragment_contact_events, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case RequestCode.READ_CALENDAR: {
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    new LoadCalendarTask(context, contact_events_view).execute(bundle);
                } else {
                    // TODO: 21-05-2020 Permission not granted. Show a message to the user
                }
            }
        }
    }

}
