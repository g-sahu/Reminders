package com.gsapps.reminders.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.gsapps.reminders.activities.SplashScreenActivity;

import static android.support.v7.preference.Preference.OnPreferenceChangeListener;
import static com.google.firebase.auth.FirebaseAuth.getInstance;
import static com.gsapps.reminders.R.string.key_connect_with_outlook;
import static com.gsapps.reminders.R.string.key_logout;
import static com.gsapps.reminders.R.xml.settings;
import static com.gsapps.reminders.services.MSAuthManager.loginOutlook;
import static com.gsapps.reminders.services.MSAuthManager.logoutOutlook;
import static com.gsapps.reminders.util.Constants.IS_LOGGED_OUT;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;

public class SettingsFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener {
    private final String LOG_TAG = getClass().getSimpleName();
    private Context context;

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
            loginOutlook(context);
            showToastMessage(context, "Logged in to Outlook");
        } else {
            logoutOutlook(context);
            showToastMessage(context, "Logged out of Outlook");
        }
    }

    public void logoutGoogle() {
        getInstance().signOut();
        Intent intent = new Intent(getContext(), SplashScreenActivity.class).putExtra(IS_LOGGED_OUT, true);
        startActivity(intent);
        getActivity().finish();
    }
}
