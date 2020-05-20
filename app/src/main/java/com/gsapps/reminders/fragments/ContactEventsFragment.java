package com.gsapps.reminders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.gsapps.reminders.R.layout.fragment_contact_events;

public class ContactEventsFragment extends Fragment {
    private final String LOG_TAG = getClass().getSimpleName();
    /*private static final int READ_REQUEST_CODE = 42, REQUEST_ACCOUNT_PICKER = 3;
    private GoogleAccountCredential credential;
    private String accountName;*/
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*accountName = getActivity().getIntent().getStringExtra(EMAIL);
        getContactEvents(accountName);*/
        return inflater.inflate(fragment_contact_events, container, false);
    }

    /*private void getContactEvents(String accountName) {
        credential = usingOAuth2(context, singleton(CALENDAR));
        credential.setSelectedAccountName(accountName);
        new LoadContactEventsTask((Activity) context)
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
                    getContactEvents(accountName);
                } else {
                    startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
                }
                break;

            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && resultData != null && resultData.getExtras() != null) {
                    String accountName = resultData.getExtras().getString(KEY_ACCOUNT_NAME);

                    if (accountName != null) {
                        getContactEvents(accountName);
                    }
                }
        }
    }*/
}
