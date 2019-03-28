package com.gsapps.reminders.listeners;

import android.util.Log;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.MsalException;
import com.microsoft.identity.client.MsalServiceException;
import com.microsoft.identity.client.MsalUiRequiredException;

import static com.gsapps.reminders.activities.HomeActivity.context;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;

public class MSAuthCallbackListener implements AuthenticationCallback {
    private final String LOG_TAG = getClass().getSimpleName();
    private static String accessToken;

    @Override
    public void onSuccess(AuthenticationResult authenticationResult) {
        Log.d(LOG_TAG, "Successfully authenticated");
        Log.d(LOG_TAG, "ID Token: " + authenticationResult.getIdToken());
        accessToken = authenticationResult.getAccessToken();
        showToastMessage(context, "Signed-in to Outlook successfully");
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

    public static String getAccessToken() {
        return accessToken;
    }
}
