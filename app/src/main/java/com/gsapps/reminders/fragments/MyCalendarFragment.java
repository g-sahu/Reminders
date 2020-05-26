package com.gsapps.reminders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.gsapps.reminders.services.LoadMyCalendarTask;

import static android.Manifest.permission.READ_CALENDAR;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.provider.CalendarContract.Calendars.ACCOUNT_NAME;
import static android.provider.CalendarContract.Calendars.ACCOUNT_TYPE;
import static com.gsapps.reminders.R.id.my_calendar_view;
import static com.gsapps.reminders.R.layout.fragment_my_calendar;
import static com.gsapps.reminders.util.ReminderUtils.hasPermission;

public class MyCalendarFragment extends Fragment {
    private Context context;
    private static final int REQUEST_READ_CALENDAR = 1;
    private final Bundle bundle = new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        String calendarsSelection = ACCOUNT_NAME + " = ? AND " + ACCOUNT_TYPE + " = ?";
        String[] calendarsSelectionArgs = {"simplygaurav07@gmail.com", "com.google"};
        bundle.putString("calendarsSelection", calendarsSelection);
        bundle.putStringArray("calendarsSelectionArgs", calendarsSelectionArgs);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(hasPermission(context, READ_CALENDAR)) {
            new LoadMyCalendarTask(context, my_calendar_view).execute(bundle);
        } else {
            requestPermissions(new String[]{READ_CALENDAR}, REQUEST_READ_CALENDAR);
        }

        return inflater.inflate(fragment_my_calendar, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_READ_CALENDAR: {
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    new LoadMyCalendarTask(context, my_calendar_view).execute(bundle);
                } else {
                    // TODO: 21-05-2020 Permission not granted. Show a message to the user
                }
            }
        }
    }

}
