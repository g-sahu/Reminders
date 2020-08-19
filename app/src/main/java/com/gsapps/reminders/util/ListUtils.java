package com.gsapps.reminders.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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

    public static <T> T getOrDefault(T object, T defaultValue) {
        return Optional.ofNullable(object)
                       .orElse(defaultValue);
    }

    public static <T, E extends Throwable> T getOrThrow(T object, Supplier<? extends E> exceptionSupplier) throws E {
        return Optional.ofNullable(object)
                       .orElseThrow(exceptionSupplier);
    }
}
