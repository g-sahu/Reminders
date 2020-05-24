package com.gsapps.reminders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static com.gsapps.reminders.R.layout.fragment_contact_events;

public class ContactEventsFragment extends Fragment {
    private static final String LOG_TAG = ContactEventsFragment.class.getSimpleName();
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(fragment_contact_events, container, false);
    }
}
