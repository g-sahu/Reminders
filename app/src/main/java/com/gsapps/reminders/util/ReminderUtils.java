package com.gsapps.reminders.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.NoArgsConstructor;

import static android.content.Context.MODE_PRIVATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.gsapps.reminders.R.string.app_name;
import static com.gsapps.reminders.R.string.key_connect_with_outlook;
import static com.gsapps.reminders.util.CalendarUtils.getStartOfDayDateTimeString;
import static java.util.TimeZone.getDefault;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ReminderUtils {
    private static Toast toast;

    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        return checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }

    public static List<String> getNotGrantedPermissions(@NonNull Context context, @NonNull Collection<String> permissions) {
        return permissions.stream()
                          .filter(permission -> !hasPermission(context, permission))
                          .collect(toList());
    }

    public static void showToastMessage(Context context, CharSequence message) {
        if(toast != null) {
            toast.cancel();
        }

        toast = makeText(context, message, LENGTH_SHORT);
        toast.show();
    }

    public static boolean isOutlookConnected(Context context) {
        return context.getSharedPreferences(context.getString(app_name), MODE_PRIVATE)
                      .getBoolean(context.getString(key_connect_with_outlook), false);
    }

    public static List<Option> getOptions() {
        List<Option> options = new ArrayList<>();
        options.add(new HeaderOption("Prefer", "outlook.timezone=\"" + getDefault().getID() + "\""));
        options.add(new QueryOption("$filter", "start/dateTime ge '" + getStartOfDayDateTimeString("yyyy-MM-dd'T'HH:mm") + "'"));
        options.add(new QueryOption("$orderby", "start/dateTime"));
        return options;
    }
}
