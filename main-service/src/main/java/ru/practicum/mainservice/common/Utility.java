package ru.practicum.mainservice.common;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.mainservice.common.exception.BadRequestException;
import ru.practicum.mainservice.event.model.State;

public class Utility {
    public static String buildPath(Object... args) {
        return StringUtils.join("/", StringUtils.join(args, ""));
    }

    public static State getState(String stateParam) {
        return State.toState(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
    }
}
