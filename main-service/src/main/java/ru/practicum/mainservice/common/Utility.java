package ru.practicum.mainservice.common;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.mainservice.common.exception.BadRequestException;
import ru.practicum.mainservice.event.model.State;

import java.util.Objects;

public class Utility {
    public static String buildPath(Object... args) {
        return StringUtils.join("/", StringUtils.join(args, ""));
    }

    public static State getState(String stateParam) {
        return State.toState(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
    }

    public static <T> void checkForNull(T param) {
        Objects.requireNonNull(param, param.toString() + "не может быть null!");
    }
}
