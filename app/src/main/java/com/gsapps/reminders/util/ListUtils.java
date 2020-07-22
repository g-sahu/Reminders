package com.gsapps.reminders.util;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ListUtils {

    @SuppressWarnings({"unchecked", "ReturnOfNull"})
    public static <T> T[] toArray(List<T> list){
        return list == null ? null : (T[]) list.toArray();
    }

    @SafeVarargs
    public static <T> ArrayList<T> asList(T... t) {
        return (ArrayList<T>) Arrays.asList(t);
    }

    public static ArrayList<String> getOrDefault(Bundle bundle, String key, ArrayList<String> defaultList) {
        final ArrayList<String> stringArrayList = bundle.getStringArrayList(key);
        return stringArrayList == null ? defaultList : stringArrayList;
    }
}
