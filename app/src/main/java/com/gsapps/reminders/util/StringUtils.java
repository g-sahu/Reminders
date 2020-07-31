package com.gsapps.reminders.util;

import androidx.annotation.Nullable;

import java.util.Collection;

import lombok.NoArgsConstructor;

import static java.util.stream.Collectors.joining;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class StringUtils {
    public static boolean isNotNullOrEmpty(String str) {
        return (str != null) && !(str.trim().isEmpty());
    }

    @Nullable
    public static String join(Collection<String> list) {
        return list == null ? null : list.stream().collect(joining(" AND "));
    }
}
