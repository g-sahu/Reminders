package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.gsapps.reminders.listeners.MSAuthCallbackListener;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.SharedPreferences.Editor;
import static com.gsapps.reminders.activities.HomeActivity.context;
import static com.gsapps.reminders.util.Constants.MSAL_ACCESS_TOKEN;
import static com.gsapps.reminders.util.Constants.MS_AUTH_CLIENT_ID;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;

public class MSAuthManager {
    private static final String LOG_TAG = "MSAuthManager";
    private final static String SCOPES [] = {"https://graph.microsoft.com/User.Read", "https://graph.microsoft.com/Calendars.Read"};
    private static String accessToken;

    public static void loginOutlook(Context context) {
        PublicClientApplication clientApp = getClientApplication(context);

        try {
            List<User> users = clientApp.getUsers();

            if (users != null && users.size() == 1) {
                clientApp.acquireTokenSilentAsync(SCOPES, users.get(0), new MSAuthCallbackListener());
            } else {
                clientApp.acquireToken((Activity) context, SCOPES, new MSAuthCallbackListener());
            }
        } catch (MsalClientException e) {
            Log.d(LOG_TAG, "MSAL Exception Generated while getting users: " + e.toString());
        }
    }

    public static void logoutOutlook(Context context) {
        PublicClientApplication clientApp = getClientApplication(context);

        try {
            for (User user : clientApp.getUsers()) {
                clientApp.remove(user);
            }

            saveAccessToken(context, null);
        } catch (MsalClientException e) {
            Log.d(LOG_TAG, "MSAL Exception Generated while getting users: " + e.toString());
            showToastMessage(context, "An error occurred while logging out of Outlook.");
        }
    }

    public static PublicClientApplication getClientApplication(Context context) {
        return new PublicClientApplication(context.getApplicationContext(), MS_AUTH_CLIENT_ID);
    }

    public static String getAccessToken() {
        if(accessToken == null) {
            SharedPreferences sharedPref = ((Activity) context).getPreferences(MODE_PRIVATE);
            accessToken = sharedPref.getString(MSAL_ACCESS_TOKEN, null);
        }

        return accessToken;
    }

    public static void saveAccessToken(Context context, String accessToken) {
        SharedPreferences sharedPref = ((Activity) context).getPreferences(MODE_PRIVATE);
        Editor editor = sharedPref.edit();
        editor.putString(MSAL_ACCESS_TOKEN, accessToken);
        editor.apply();
        MSAuthManager.accessToken = accessToken;
    }
}
