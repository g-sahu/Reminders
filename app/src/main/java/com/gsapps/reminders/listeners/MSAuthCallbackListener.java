package com.gsapps.reminders.listeners;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.MsalServiceException;
import com.microsoft.identity.client.MsalUiRequiredException;

import static android.support.v4.content.LocalBroadcastManager.getInstance;
import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.gsapps.reminders.R.string.key_connect_with_outlook;
import static com.gsapps.reminders.activities.HomeActivity.context;
import static com.gsapps.reminders.services.MSAuthManager.saveAccessToken;
import static com.gsapps.reminders.util.Constants.ACTION_MSAL_ACCESS_TOKEN_ACQUIRED;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;

public class MSAuthCallbackListener implements AuthenticationCallback {
    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    public void onSuccess(AuthenticationResult authenticationResult) {
        Log.d(LOG_TAG, "Successfully authenticated");
        Log.d(LOG_TAG, "ID Token: " + authenticationResult.getIdToken());
        saveAccessToken(context, authenticationResult.getAccessToken());

        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(context.getString(key_connect_with_outlook), true);
        editor.apply();

        sendBroadcast();
        showToastMessage(context, "Logged in to Outlook");
    }

    @Override
    public void onError(MsalException e) {
        Log.d(LOG_TAG, "Authentication failed: " + e.toString());
        showToastMessage(context, "MS authentication failed: " + e.toString());

        if (e instanceof MsalClientException) {
            /* Exception inside MSAL, more info inside MsalError.java */
        } else if (e instanceof MsalServiceException) {
            /* Exception when communicating with the STS, likely config issue */
        } else if (e instanceof MsalUiRequiredException) {
            /* Tokens expired or no session, retry with interactive */
        }
    }

    @Override
    public void onCancel() {
        Log.d(LOG_TAG, "User cancelled login.");
        showToastMessage(context, "User cancelled login.");
    }

    private void sendBroadcast() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_MSAL_ACCESS_TOKEN_ACQUIRED);
        getInstance(context).sendBroadcast(broadcastIntent);
    }
}
