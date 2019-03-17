package com.gsapps.reminders.activities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.gsapps.reminders.fragments.CalendarFragment;
import com.gsapps.reminders.fragments.ContactEventsFragment;
import com.gsapps.reminders.fragments.SettingsFragment;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private Fragment calendarFragment, contactEventsFragment, settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            case fragment_calendar:
                calendarFragment = (calendarFragment == null) ? new CalendarFragment() : calendarFragment;
                fragmentTransaction.replace(fragment_content, calendarFragment, CALENDAR_FRAGMENT);
                break;

            case fragment_contact_events:
                contactEventsFragment = (contactEventsFragment == null) ? new ContactEventsFragment() : contactEventsFragment;
                fragmentTransaction.replace(fragment_content, contactEventsFragment, CONTACT_EVENTS_FRAGMENT);
                break;

            case fragment_settings:
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
        Intent intent = new Intent(this, SplashScreenActivity.class).putExtra(IS_LOGGED_OUT, true);
        startActivity(intent);
        finish();
    }
}
