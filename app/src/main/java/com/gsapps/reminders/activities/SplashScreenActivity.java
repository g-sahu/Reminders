package com.gsapps.reminders.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static com.gsapps.reminders.R.layout.activity_splash_screen;

public class SplashScreenActivity extends Activity {
    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_splash_screen);
        Log.d(LOG_TAG, "SplashScreenActivity created");
        Intent intent = new Intent(this, HomeActivity.class);
        Log.d(LOG_TAG, "Starting " + intent.getComponent().getShortClassName());
        startActivity(intent);
        Log.d(LOG_TAG, "Exiting " + getLocalClassName());
    }
}
