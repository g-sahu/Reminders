package com.gsapps.reminders.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import com.gsapps.reminders.fragments.CalendarFragment;
import com.gsapps.reminders.fragments.ContactEventsFragment;
import com.gsapps.reminders.fragments.MeetingsFragment;
import com.gsapps.reminders.fragments.SettingsFragment;
import com.gsapps.reminders.fragments.TestFragment;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;
import static com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import static com.gsapps.reminders.R.id;
import static com.gsapps.reminders.R.id.*;
import static com.gsapps.reminders.R.layout.activity_home;
import static com.gsapps.reminders.R.string.drawer_close;
import static com.gsapps.reminders.R.string.drawer_open;
import static com.gsapps.reminders.R.string.my_calendar;
import static com.gsapps.reminders.services.MSAuthManager.getClientApplication;
import static com.gsapps.reminders.services.MSAuthManager.loginOutlook;
import static com.gsapps.reminders.util.Constants.*;

public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment calendarFragment, contactEventsFragment, meetingsFragment, settingsFragment, testFragment;
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
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.getHeaderView(0);
        Intent intent = getIntent();
        loadProfilePic(headerView.findViewById(profile_pic), intent.getParcelableExtra(PHOTO_URL));
        ((TextView) headerView.findViewById(display_name)).setText(intent.getStringExtra(DISPLAY_NAME));

        //Setting up the default fragment
        calendarFragment = new CalendarFragment();
        replaceFragment();
        setTitle(my_calendar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        selectDrawerItem(menuItem);
        return true;
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

            case item_test:
                testFragment = (testFragment == null) ? new TestFragment() : testFragment;
                fragmentTransaction.replace(fragment_content, testFragment, "TEST_FRAGMENT");
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

    /* Handles the redirect from the System Browser */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getClientApplication(context).handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }

    private void loadProfilePic(CircleImageView profilePic, Uri uri) {
        with(this)
                .load(uri)
                .crossFade()
                .diskCacheStrategy(ALL)
                .into(profilePic);
    }

    private void replaceFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(fragment_content, calendarFragment, CALENDAR_FRAGMENT)
                .commit();
    }

    public void connectWithOutlook(View view) {
        findViewById(connect_with_outlook).setVisibility(GONE);
        findViewById(meetings_view).setVisibility(VISIBLE);
        loginOutlook(context);
    }
}
