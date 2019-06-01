package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.gsapps.reminders.listeners.MSAuthCallbackListener;
import com.gsapps.reminders.util.ReminderUtils;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.SharedPreferences.Editor;
import static com.gsapps.reminders.activities.HomeActivity.context;
import static com.gsapps.reminders.util.Constants.MSAL_ACCESS_TOKEN;
import static com.gsapps.reminders.util.Constants.MS_AUTH_CLIENT_ID;
import static com.gsapps.reminders.util.ReminderUtils.*;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;
import static java.util.Arrays.*;

public class MSAuthManager
        implements OnSuccessListener<AuthResult>, OnFailureListener, OnCanceledListener, OnCompleteListener<AuthResult> {
    private static final String LOG_TAG = "MSAuthManager";
    private final static String[] SCOPES = {"User.Read", "Calendars.Read"};
    private static String accessToken;

    public void loginOutlook(Context context) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();

        if (pendingResultTask == null) {
            OAuthProvider.Builder provider = OAuthProvider.newBuilder("microsoft.com");
            provider.setScopes(asList(SCOPES));

            firebaseAuth.startActivityForSignInWithProvider((Activity) context, provider.build())
                        .addOnSuccessListener(this)
                        .addOnFailureListener(this)
                        .addOnCanceledListener(this);
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

    @Override
    public void onSuccess(AuthResult authResult) {
        showToastMessage(context, "Logged in to Microsoft successfully!");
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        showToastMessage(context, "Failed to log in to Microsoft: " + e.getMessage());
        Log.e(LOG_TAG, e.getMessage());
    }

    @Override
    public void onCanceled() {
        showToastMessage(context, "Log in to Microsoft cancelled!");
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        showToastMessage(context, "Log in to Microsoft completed!");
    }
}
