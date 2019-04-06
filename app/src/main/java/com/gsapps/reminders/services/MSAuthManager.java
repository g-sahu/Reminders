package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.gsapps.reminders.listeners.MSAuthCallbackListener;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import java.util.List;

import static com.gsapps.reminders.util.Constants.MS_AUTH_CLIENT_ID;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;

public class MSAuthManager {
    private static final String LOG_TAG = "MSAuthManager";
    private final static String SCOPES [] = {"https://graph.microsoft.com/User.Read", "https://graph.microsoft.com/Calendars.Read"};

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
        } catch (MsalClientException e) {
            Log.d(LOG_TAG, "MSAL Exception Generated while getting users: " + e.toString());
            showToastMessage(context, "An error occurred while logging out of Outlook.");
        }
    }

    public static PublicClientApplication getClientApplication(Context context) {
        return new PublicClientApplication(context.getApplicationContext(), MS_AUTH_CLIENT_ID);
    }
}
