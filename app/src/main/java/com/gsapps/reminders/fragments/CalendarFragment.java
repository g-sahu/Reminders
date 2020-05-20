package com.gsapps.reminders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.gsapps.reminders.R.layout.fragment_calendar;

public class CalendarFragment extends Fragment {
    private Context context;
    /*private static final int READ_REQUEST_CODE = 42, REQUEST_ACCOUNT_PICKER = 3;
    private GoogleAccountCredential credential;
    private String accountName;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*accountName = getActivity().getIntent().getStringExtra(EMAIL);
        getCalendarEvents(accountName);*/
        return inflater.inflate(fragment_calendar, container, false);
    }

    /*private void getCalendarEvents(String accountName) {
        credential = usingOAuth2(context, singleton(CALENDAR));
        credential.setSelectedAccountName(accountName);
        new LoadGoogleCalendarTask((Activity) context)
                .execute(getCalendar(credential, getString(app_name)));
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
    }*/

}
