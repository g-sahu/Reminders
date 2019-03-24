package com.gsapps.reminders.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import com.gsapps.reminders.fragments.CalendarFragment;
import com.gsapps.reminders.fragments.ContactEventsFragment;
import com.gsapps.reminders.fragments.MeetingsFragment;
import com.gsapps.reminders.fragments.SettingsFragment;
import com.gsapps.reminders.listeners.MSAuthCallbackListener;
import com.microsoft.identity.client.MsalClientException;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.User;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;
import static com.google.firebase.auth.FirebaseAuth.getInstance;
import static com.gsapps.reminders.R.id;
import static com.gsapps.reminders.R.id.*;
import static com.gsapps.reminders.R.layout.activity_home;
import static com.gsapps.reminders.R.string.drawer_close;
import static com.gsapps.reminders.R.string.drawer_open;
import static com.gsapps.reminders.R.string.my_calendar;
import static com.gsapps.reminders.util.Constants.*;

public class HomeActivity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment calendarFragment, contactEventsFragment, meetingsFragment, settingsFragment;

    private final static String SCOPES [] = {"https://graph.microsoft.com/User.Read"};
    private final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";
    private PublicClientApplication sampleApp;
    public static Context context; // TODO: 24-03-2019 To be changed to a better way to access activity context in other classes.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(activity_home);

        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, drawer_open,  drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        // Setup navigation drawer view
        NavigationView navView = findViewById(nav_view);
        View headerView = navView.getHeaderView(0);
        CircleImageView profilePic = headerView.findViewById(profile_pic);
        Intent intent = getIntent();

        with(this)
            .load((Uri) intent.getParcelableExtra(PHOTO_URL))
            .crossFade()
            .diskCacheStrategy(ALL)
            .into(profilePic);

        TextView displayName = headerView.findViewById(display_name);
        displayName.setText(intent.getStringExtra(DISPLAY_NAME));

        navView.setNavigationItemSelectedListener(menuItem -> {
            selectDrawerItem(menuItem);
            return true;
        });

        //Setting up the default fragment
        calendarFragment = new CalendarFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragment_content, calendarFragment, CALENDAR_FRAGMENT)
                .commit();

        setTitle(my_calendar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void selectDrawerItem(MenuItem menuItem) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch(menuItem.getItemId()) {
            case item_calendar:
                calendarFragment = (calendarFragment == null) ? new CalendarFragment() : calendarFragment;
                fragmentTransaction.replace(fragment_content, calendarFragment, CALENDAR_FRAGMENT);
                break;

            case item_contact_events:
                contactEventsFragment = (contactEventsFragment == null) ? new ContactEventsFragment() : contactEventsFragment;
                fragmentTransaction.replace(fragment_content, contactEventsFragment, CONTACT_EVENTS_FRAGMENT);
                break;

            case item_meetings:
                meetingsFragment = (meetingsFragment == null) ? new MeetingsFragment() : meetingsFragment;
                fragmentTransaction.replace(fragment_content, meetingsFragment, MEETINGS_FRAGMENT);
                break;

            case item_settings:
                settingsFragment = (settingsFragment == null) ? new SettingsFragment() : settingsFragment;
                fragmentTransaction.replace(fragment_content, settingsFragment, SETTINGS_FRAGMENT);
                break;
        }

        fragmentTransaction.commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void logout(View view) {
        getInstance().signOut();
        Intent intent = new Intent(context, SplashScreenActivity.class).putExtra(IS_LOGGED_OUT, true);
        startActivity(intent);
        finish();
    }

    public void connectWithOutlook(View view) {
        Switch switchButton = (Switch) view;

        if(switchButton.isChecked()) {
            loginOutlook();
        } else {
            logoutOutlook();
        }
    }

    private void loginOutlook() {
        sampleApp = new PublicClientApplication(getApplicationContext(), MS_AUTH_CLIENT_ID);

        try {
            List<User> users = sampleApp.getUsers();

            if (users != null && users.size() == 1) {
                sampleApp.acquireTokenSilentAsync(SCOPES, users.get(0), new MSAuthCallbackListener());
            } else {
                sampleApp.acquireToken(this, SCOPES, new MSAuthCallbackListener());
            }
        } catch (MsalClientException e) {
            Log.d(LOG_TAG, "MSAL Exception Generated while getting users: " + e.toString());
        } catch (IndexOutOfBoundsException e) {
            Log.d(LOG_TAG, "User at this position does not exist: " + e.toString());
        }
    }

    /* Handles the redirect from the System Browser */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        sampleApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }

    private void logoutOutlook() {

    }
}
