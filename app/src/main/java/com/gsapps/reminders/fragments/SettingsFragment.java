package com.gsapps.reminders.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.gsapps.reminders.services.MSAuthManager;

import static androidx.preference.Preference.OnPreferenceChangeListener;
import static androidx.preference.Preference.OnPreferenceClickListener;
import static com.gsapps.reminders.R.string.key_connect_with_outlook;
import static com.gsapps.reminders.R.string.key_logout;
import static com.gsapps.reminders.R.xml.settings;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;

public class SettingsFragment extends PreferenceFragmentCompat
        implements OnPreferenceChangeListener, OnPreferenceClickListener {
    private static final String LOG_TAG = SettingsFragment.class.getSimpleName();
    private MSAuthManager msAuthManager;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (Activity) getContext();
        msAuthManager = new MSAuthManager(activity);
        findPreference(getString(key_connect_with_outlook)).setOnPreferenceChangeListener(this);
        //findPreference(getString(key_logout)).setOnPreferenceChangeListener(this);
        findPreference(getString(key_logout)).setOnPreferenceClickListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(settings, rootKey);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if(preference.getKey().equals(getString(key_connect_with_outlook))) {
            connectWithOutlook((boolean) o);
            return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals(getString(key_logout))) {
            //logoutGoogle();
            return true;
        }

        return false;
    }

    public void connectWithOutlook(boolean isSwitchedOn) {
        if(isSwitchedOn) {
            msAuthManager.loginOutlook(activity);
        } else {
            msAuthManager.logoutOutlook();
            showToastMessage(activity, "Logged out of Outlook");
        }
    }
}
