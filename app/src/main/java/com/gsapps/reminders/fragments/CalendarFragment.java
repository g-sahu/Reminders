package com.gsapps.reminders.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.gsapps.reminders.services.LoadGoogleCalendarTask;
import com.gsapps.reminders.util.Constants;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentResolver.SYNC_EXTRAS_EXPEDITED;
import static android.content.ContentResolver.SYNC_EXTRAS_MANUAL;
import static android.content.ContentResolver.requestSync;
import static android.content.Context.ACCOUNT_SERVICE;
import static com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential.usingOAuth2;
import static com.google.api.services.calendar.CalendarScopes.CALENDAR;
import static com.gsapps.reminders.R.layout.fragment_calendar;
import static com.gsapps.reminders.R.string.account_type;
import static com.gsapps.reminders.R.string.app_name;
import static com.gsapps.reminders.R.string.authority;
import static com.gsapps.reminders.util.Constants.EMAIL;
import static com.gsapps.reminders.util.ReminderUtils.getCalendar;
import static com.gsapps.reminders.util.ReminderUtils.getResourceString;
import static java.util.Collections.singleton;

public class CalendarFragment extends Fragment {
    private Context context;
    private static final int READ_REQUEST_CODE = 42, REQUEST_ACCOUNT_PICKER = 3;
    private GoogleAccountCredential credential;
    private String accountName;
    private Account mAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        // Create the dummy account
        mAccount = createSyncAccount(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountName = getActivity().getIntent().getStringExtra(EMAIL);
        //getCalendarEvents(accountName);
        syncCalendar();
        return inflater.inflate(fragment_calendar, container, false);
    }

    private void syncCalendar() {
        credential = usingOAuth2(context, singleton(CALENDAR));
        credential.setSelectedAccountName(accountName);

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(SYNC_EXTRAS_EXPEDITED, true);
        requestSync(mAccount, getResourceString(context, authority), settingsBundle);
    }

    private void getCalendarEvents(String accountName) {
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
    }

    private Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account("Reminders", getResourceString(context, account_type));

        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            //syncCalendar();
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount;
    }



}
