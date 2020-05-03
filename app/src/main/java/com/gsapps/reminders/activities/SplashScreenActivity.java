package com.gsapps.reminders.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.view.View.OnClickListener;
import static com.google.android.gms.auth.api.Auth.GOOGLE_SIGN_IN_API;
import static com.google.android.gms.auth.api.Auth.GoogleSignInApi;
import static com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN;
import static com.google.android.gms.common.api.GoogleApiClient.Builder;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import static com.google.firebase.auth.FirebaseAuth.getInstance;
import static com.google.firebase.auth.GoogleAuthProvider.getCredential;
import static com.gsapps.reminders.R.id.sign_in_button;
import static com.gsapps.reminders.R.layout.activity_splash_screen;
import static com.gsapps.reminders.R.string.*;
import static com.gsapps.reminders.util.Constants.*;
import static com.gsapps.reminders.util.ReminderUtils.showToastMessage;

public class SplashScreenActivity extends AppCompatActivity
        implements ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {
    private final String LOG_TAG = getClass().getSimpleName();
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth fAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_splash_screen);
        findViewById(sign_in_button).setOnClickListener(this);
        googleApiClient = getGoogleApiClient(getGoogleSignInOptions());
        fAuth = getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();

        if(user != null) {
            Intent intent = createIntent(this, HomeActivity.class, user.getDisplayName(), user.getPhotoUrl(), user.getEmail());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());

        if(isConnected) {
            Intent signInIntent = GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            showToastMessage(this, getString(no_internet));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                firebaseAuthWithGoogle(result.getSignInAccount());
                showToastMessage(this, getString(sign_in_success));
            } else {
                Log.e(LOG_TAG, getString(sign_in_failure));
                Log.e(LOG_TAG, "Status code: " + result.getStatus().getStatusCode());
                Log.e(LOG_TAG, "Status msg: " + result.getStatus().getStatusMessage());
                showToastMessage(this, getString(sign_in_failure));
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = getCredential(account.getIdToken(), null);

        fAuth.signInWithCredential(credential)
             .addOnCompleteListener(this, task -> {
                 if (task.isSuccessful()) {
                     Intent intent = createIntent(SplashScreenActivity.this, HomeActivity.class,
                             account.getDisplayName(), account.getPhotoUrl(), account.getEmail());
                     startActivity(intent);
                     finish();
                 } else {
                     Log.e(LOG_TAG, "Firebase authentication failed.", task.getException());
                     showToastMessage(SplashScreenActivity.this, getString(auth_failed));
                 }
             });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(getIntent().getBooleanExtra(IS_LOGGED_OUT, false)) {
            GoogleSignInApi.signOut(googleApiClient);
            showToastMessage(this, getString(logout_msg));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Connection to Google API client failed!");
        Log.e(LOG_TAG, connectionResult.getErrorMessage());
        showToastMessage(this, getString(connection_failure));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.unregisterConnectionCallbacks(this);
    }


    private GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_AUTH_CLIENT_ID)
                .requestEmail()
                .build();
    }

    private GoogleApiClient getGoogleApiClient(GoogleSignInOptions gso) {
        return new Builder(this)
                .enableAutoManage(this, this)
                .addApi(GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .build();
    }

    private Intent createIntent(Context context, Class<?> clazz, String displayName, Uri photoUrl, String email) {
        return new Intent(context, clazz)
                .putExtra(DISPLAY_NAME, displayName)
                .putExtra(PHOTO_URL, photoUrl)
                .putExtra(EMAIL, email);
    }
}
