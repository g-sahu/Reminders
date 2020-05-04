package com.gsapps.reminders.converters;

import androidx.room.TypeConverter;

public class BooleanConverter {

    @TypeConverter
    public static boolean toBoolean(int value) {
        switch (value) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new IllegalArgumentException("Invalid value for boolean field: " + value);
        }
    }

    @TypeConverter
    public static int fromBoolean(boolean value) {
        return value ? 1 : 0;
    }
}
