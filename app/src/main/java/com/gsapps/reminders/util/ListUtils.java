package com.gsapps.reminders.util;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ListUtils {

    public static <T> T[] toArray(@NonNull List<T> list, @NonNull T[] t) {
        return (T[]) list.toArray(t);
    }

    @SafeVarargs
    public static <T> ArrayList<T> asList(T... t) {
        return new ArrayList<>(Arrays.asList(t));
    }

    public static ArrayList<String> getOrDefault(@NonNull Bundle bundle, String key, ArrayList<String> defaultList) {
        return Optional.ofNullable(bundle.getStringArrayList(key))
                       .orElse(defaultList);
    }
}
