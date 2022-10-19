package ru.practicum.statsservice.common;

import java.util.Objects;

public class Utility {

    public static <T> void checkForNull(T param) {
        Objects.requireNonNull(param, param.toString() + "не может быть null!");
    }
}
