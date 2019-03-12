package com.gsapps.reminders.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static android.widget.Toast.*;

public class ReminderUtils {
    private static Toast toast;

    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        return checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }

    public static List<String> checkPermissions(@NonNull Context context, @NonNull List<String> permissions) {
        List<String> notGrantedPermissions = new ArrayList<>(permissions.size());

        for(String permission: permissions) {
            if(!hasPermission(context, permission)) {
                notGrantedPermissions.add(permission);
            }
        }

        return notGrantedPermissions;
    }

    @SuppressLint("Recycle")
    public static Cursor[] getContentFromProvider(Context context, Uri[] uris, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursors[] = new Cursor[uris.length];
        ContentResolver contentResolver = context.getContentResolver();

        for (int i=0; i<uris.length; i++) {
            cursors[i] = contentResolver.query(uris[i], projection, selection, selectionArgs, sortOrder);
        }

        return cursors;
    }

    public static void showToastMessage(Context context, String message) {
        if(toast != null) {
            toast.cancel();
        }

        toast = makeText(context, message, LENGTH_SHORT);
        toast.show();
    }
}
