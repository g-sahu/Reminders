package com.gsapps.reminders.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.gsapps.reminders.fragments.CalendarFragment;

import static com.gsapps.reminders.R.id;
import static com.gsapps.reminders.R.id.drawer_layout;
import static com.gsapps.reminders.R.id.fragment_calendar;
import static com.gsapps.reminders.R.id.fragment_content;
import static com.gsapps.reminders.R.id.nav_view;
import static com.gsapps.reminders.R.layout.activity_home;
import static com.gsapps.reminders.R.string.calendar;
import static com.gsapps.reminders.R.string.drawer_close;
import static com.gsapps.reminders.R.string.drawer_open;
import static com.gsapps.reminders.util.Constants.CALENDAR_FRAGMENT;

public class HomeActivity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment calendarFragment;

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
        navView.setNavigationItemSelectedListener(menuItem -> {
            selectDrawerItem(menuItem);
            return true;
        });

        //Setting up the default fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        calendarFragment = new CalendarFragment();

        fragmentManager.beginTransaction()
                       .replace(fragment_content, calendarFragment, CALENDAR_FRAGMENT)
                       .commit();

        setTitle(calendar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch(menuItem.getItemId()) {
            case fragment_calendar:
                calendarFragment = (calendarFragment == null) ? new CalendarFragment() : calendarFragment;
                fragmentTransaction.replace(fragment_content, calendarFragment, CALENDAR_FRAGMENT);
                break;
        }

        fragmentTransaction.commit();

        // Highlight the selected item has been done by NavigationView
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

}
