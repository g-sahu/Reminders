package com.gsapps.reminders.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import static com.gsapps.reminders.R.layout.activity_splash_screen;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String LOG_TAG = SplashScreenActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_splash_screen);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
