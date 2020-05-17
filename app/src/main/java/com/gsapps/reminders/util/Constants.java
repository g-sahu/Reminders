package com.gsapps.reminders.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Constants {
    // TODO: 24-03-2019 Store the client ids in a secure way
    public static final String GOOGLE_AUTH_CLIENT_ID = "903110263645-q7spnpsb0eof9c374s0pft1d6nsvcn63.apps.googleusercontent.com";
    public static final String MS_AUTH_CLIENT_ID = "86773c8b-b482-474b-abf4-c7e2599611f8";
    public static final String CALENDAR_FRAGMENT = "CALENDAR_FRAGMENT";
    public static final String CONTACT_EVENTS_FRAGMENT = "CONTACT_EVENTS_FRAGMENT";
    public static final String MEETINGS_FRAGMENT = "MEETINGS_FRAGMENT";
    public static final String SETTINGS_FRAGMENT = "SETTINGS_FRAGMENT";
    public static final String PHOTO_URL = "PHOTO_URL";
    public static final String DISPLAY_NAME = "DISPLAY_NAME";
    public static final String EMAIL = "EMAIL";
    public static final int REQUEST_AUTHORIZATION = 2;
    public static final String IS_LOGGED_OUT = "isLoggedOut";
    public static final String ACTION_MSAL_ACCESS_TOKEN_ACQUIRED = "ACTION_MSAL_ACCESS_TOKEN_ACQUIRED";
    public static final String MSAL_ACCESS_TOKEN = "MSAL_ACCESS_TOKEN";
}
