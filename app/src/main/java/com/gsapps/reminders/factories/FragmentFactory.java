package com.gsapps.reminders.factories;

import androidx.fragment.app.Fragment;
import com.gsapps.reminders.fragments.ContactEventsFragment;
import com.gsapps.reminders.fragments.MeetingsFragment;
import com.gsapps.reminders.fragments.MyCalendarFragment;
import com.gsapps.reminders.fragments.SettingsFragment;
import com.gsapps.reminders.util.enums.FragmentTag;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class FragmentFactory {
    private static FragmentFactory fragmentFactory = null;

    public static FragmentFactory getFragmentFactory() {
        if(fragmentFactory == null) {
            synchronized (FragmentFactory.class) {
                if(fragmentFactory == null) {
                    fragmentFactory = new FragmentFactory();
                }
            }
        }

        return fragmentFactory;
    }

    public Fragment createFragment(FragmentTag fragmentTag) {
        switch (fragmentTag) {
            case MY_CALENDAR_FRAGMENT:
                return new MyCalendarFragment();

            case CONTACT_EVENTS_FRAGMENT:
                return new ContactEventsFragment();

            case MEETINGS_FRAGMENT:
                return new MeetingsFragment();

            case SETTINGS_FRAGMENT:
                return new SettingsFragment();

            default:
                return null;
        }
    }
}
