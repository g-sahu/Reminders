package com.gsapps.reminders.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.calendar.Calendar;
import com.gsapps.reminders.services.LoadGoogleCalendarTask;
import com.gsapps.reminders.util.Constants;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.app.Activity.RESULT_OK;
import static com.google.api.client.extensions.android.http.AndroidHttp.newCompatibleTransport;
import static com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential.usingOAuth2;
import static com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance;
import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static com.gsapps.reminders.R.layout.fragment_calendar;
import static com.gsapps.reminders.R.string.app_name;
import static com.gsapps.reminders.util.Constants.EMAIL;
import static java.util.Collections.singleton;

public class CalendarFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    private Context context;
    private static final int READ_REQUEST_CODE = 42, REQUEST_ACCOUNT_PICKER = 3;
    private GoogleAccountCredential credential;
    private String accountName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountName = getActivity().getIntent().getStringExtra(EMAIL);
        getCalendarEvents(accountName);
        return inflater.inflate(fragment_calendar, container, false);
    }

    private void getCalendarEvents(String accountName) {
        credential = usingOAuth2(context, singleton(CALENDAR));
        credential.setSelectedAccountName(accountName);

        Calendar service = new Calendar.Builder(newCompatibleTransport(), getDefaultInstance(), credential)
                                        .setApplicationName(getString(app_name))
                                        .build();

        LoadGoogleCalendarTask calendarTask = new LoadGoogleCalendarTask((Activity) context);
        calendarTask.execute(service);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        switch (requestCode) {
            case READ_REQUEST_CODE:
                if (resultCode == RESULT_OK && resultData != null) {
                    // TODO: 11-03-2019 Show a message when user does not grant the permissions
                }
                break;

            case Constants.REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getCalendarEvents(accountName);
                } else {
                    startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
                }
                break;

            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && resultData != null && resultData.getExtras() != null) {
                    String accountName = resultData.getExtras().getString(KEY_ACCOUNT_NAME);

                    if (accountName != null) {
                        getCalendarEvents(accountName);
                    }
                }
        }
    }

}
