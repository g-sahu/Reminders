package com.gsapps.reminders.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gsapps.reminders.RemindersApplication;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.microsoft.identity.client.exception.MsalUiRequiredException;

import java.util.Optional;

import static android.content.Context.MODE_PRIVATE;
import static androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance;
import static com.gsapps.reminders.R.string.key_connect_with_outlook;
import static com.gsapps.reminders.util.Constants.ACTION_MSAL_ACCESS_TOKEN_ACQUIRED;
import static com.gsapps.reminders.util.Constants.MSAL_ACCESS_TOKEN;
import static com.gsapps.reminders.util.Constants.MSAL_ERROR_MSG;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;
import static com.microsoft.graph.logger.LoggerLevel.DEBUG;
import static com.microsoft.identity.client.ISingleAccountPublicClientApplication.SignOutCallback;
import static java.lang.String.format;

public class MSAuthManager implements IAuthenticationProvider {
    private static final String LOG_TAG = "MSAuthManager";
    private final static String[] SCOPES = {"https://graph.microsoft.com/User.Read", "https://graph.microsoft.com/Calendars.Read"};
    private final Context appContext;
    private final String appName;
    private final ISingleAccountPublicClientApplication singleAccountApp;
    private IGraphServiceClient graphServiceClient;
    private IAccount account;
    private String accessToken;

    public MSAuthManager(Activity activity) {
        appContext = activity.getApplicationContext();
        RemindersApplication remindersApplication = (RemindersApplication) activity.getApplication();
        appName = remindersApplication.getAppName();
        singleAccountApp = remindersApplication.getSingleAccountApp();
    }

    public synchronized IGraphServiceClient getGraphServiceClient() {
        if (graphServiceClient == null) {
            graphServiceClient = GraphServiceClient.builder()
                                                   .authenticationProvider(this)
                                                   .buildClient();

            graphServiceClient.getLogger().setLoggingLevel(DEBUG);
        }

        return graphServiceClient;
    }

    @Override
    public void authenticateRequest(IHttpRequest request)  {
        request.addHeader("Authorization", "Bearer " + getAccessToken());
        Log.i(LOG_TAG, "Authenticating MS Graph request: " + request);
    }

    public void loginOutlook(Activity activity) {
        if (account == null) {
            singleAccountApp.signIn(activity, null, SCOPES, new AuthenticationCallbackListener());
        } else {
            try {
                String authority = singleAccountApp.getConfiguration().getDefaultAuthority().getAuthorityURL().toString();
                IAuthenticationResult result = singleAccountApp.acquireTokenSilent(SCOPES, authority);
                postAuthProcess(result);
            } catch (MsalException e) {
                Log.e(LOG_TAG, "Exception while acquiring token silently. " + format(MSAL_ERROR_MSG, e.getErrorCode(), e.getMessage()));
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "Exception while acquiring token silently: " + e.getMessage());
            }
        }
    }

    public void logoutOutlook() {
        singleAccountApp.signOut(new SignOutCallbackListener());
        saveAccessToken(null);
    }

    private void postAuthProcess(IAuthenticationResult authenticationResult) {
        String accessToken = authenticationResult.getAccessToken();
        account = authenticationResult.getAccount();
        Log.d(LOG_TAG, "Authenticated successfully ");
        saveAccessToken(accessToken);
        saveOutlookSettings();
        sendBroadcast();
        showToastMessage(appContext, "Logged in to Outlook");
    }

    public void saveAccessToken(String accessToken) {
        appContext.getSharedPreferences(appName, MODE_PRIVATE)
                  .edit()
                  .putString(MSAL_ACCESS_TOKEN, accessToken)
                  .apply();

        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return Optional.ofNullable(accessToken)
                       .orElse(appContext.getSharedPreferences(appName, MODE_PRIVATE)
                                         .getString(MSAL_ACCESS_TOKEN, null));
    }

    private void sendBroadcast() {
        getInstance(appContext)
                .sendBroadcast(new Intent(ACTION_MSAL_ACCESS_TOKEN_ACQUIRED));
    }

    private void saveOutlookSettings() {
        appContext.getSharedPreferences(appName, MODE_PRIVATE)
                  .edit()
                  .putBoolean(appContext.getString(key_connect_with_outlook), true)
                  .apply();
    }

    private class AuthenticationCallbackListener implements AuthenticationCallback {
        @Override
        public void onSuccess(IAuthenticationResult authenticationResult) {
            postAuthProcess(authenticationResult);
        }

        @Override
        public void onError(MsalException e) {
            Log.e(LOG_TAG, "Authentication failed: " + e);
            showToastMessage(appContext, "MS authentication failed. " + format(MSAL_ERROR_MSG, e.getErrorCode(), e.getMessage()));

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
            Log.i(LOG_TAG, "User cancelled login.");
            showToastMessage(appContext, "User cancelled login.");
        }
    }

    private class SignOutCallbackListener implements SignOutCallback {
        @Override
        public void onSignOut() {
            Log.i(LOG_TAG, "Account removed successfully");
            showToastMessage(appContext, "Logged out of Outlook");
        }

        @Override
        public void onError(@NonNull MsalException e) {
            Log.e(LOG_TAG, "Exception while removing account. " + format(MSAL_ERROR_MSG, e.getErrorCode(), e.getMessage()));
        }
    }
}
