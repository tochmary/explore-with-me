package ru.practicum.statsservice.common;

import java.util.Objects;

public class Utility {

    public static <T> void checkForNull(T param, String paramName) {
        Objects.requireNonNull(param, paramName + " не может быть null!");
    }
}
