package com.gsapps.reminders.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import com.gsapps.reminders.activities.SplashScreenActivity;
import com.gsapps.reminders.listeners.MSAuthCallbackListener;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import java.util.List;

import static android.support.v7.preference.Preference.OnPreferenceChangeListener;
import static com.google.firebase.auth.FirebaseAuth.getInstance;
import static com.gsapps.reminders.R.string.key_connect_with_outlook;
import static com.gsapps.reminders.R.string.key_logout;
import static com.gsapps.reminders.R.xml.settings;
import static com.gsapps.reminders.util.Constants.IS_LOGGED_OUT;
import static com.gsapps.reminders.util.Constants.MS_AUTH_CLIENT_ID;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;

public class SettingsFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener {
    private final String LOG_TAG = getClass().getSimpleName();
    private Context context;
    private final static String SCOPES [] = {"https://graph.microsoft.com/User.Read", "https://graph.microsoft.com/Calendars.Read"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        findPreference(getString(key_connect_with_outlook)).setOnPreferenceChangeListener(this);
        findPreference(getString(key_logout)).setOnPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(settings, rootKey);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if(preference.getKey().equals(getString(key_connect_with_outlook))) {
            connectWithOutlook((boolean) o);
        } else if(preference.getKey().equals(getString(key_logout))) {
            logoutGoogle();
        }

        return true;
    }


    public void connectWithOutlook(boolean isSwitchedOn) {
        if(isSwitchedOn) {
            loginOutlook();
        } else {
            logoutOutlook();
        }
    }

    private void loginOutlook() {
        PublicClientApplication sampleApp = new PublicClientApplication(context.getApplicationContext(), MS_AUTH_CLIENT_ID);

        try {
            List<User> users = sampleApp.getUsers();

            if (users != null && users.size() == 1) {
                sampleApp.acquireTokenSilentAsync(SCOPES, users.get(0), new MSAuthCallbackListener());
            } else {
                sampleApp.acquireToken((Activity) context, SCOPES, new MSAuthCallbackListener());
            }
        } catch (MsalClientException e) {
            Log.d(LOG_TAG, "MSAL Exception Generated while getting users: " + e.toString());
        }
    }

    private void logoutOutlook() {
        PublicClientApplication sampleApp = new PublicClientApplication(context.getApplicationContext(), MS_AUTH_CLIENT_ID);

        try {
            for (User user : sampleApp.getUsers()) {
                sampleApp.remove(user);
            }

            showToastMessage(context, "Logged out of Outlook");
        } catch (MsalClientException e) {
            Log.d(LOG_TAG, "MSAL Exception Generated while getting users: " + e.toString());
            showToastMessage(context, "An error occurred while logging out of Outlook.");
        }
    }

    public void logoutGoogle() {
        getInstance().signOut();
        Intent intent = new Intent(getContext(), SplashScreenActivity.class).putExtra(IS_LOGGED_OUT, true);
        startActivity(intent);
        getActivity().finish();
    }
}
