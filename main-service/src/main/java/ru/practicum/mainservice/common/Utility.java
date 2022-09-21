package ru.practicum.mainservice.common;

import org.apache.commons.lang3.StringUtils;

public class Utility {
    public static String buildPath(Object... args) {
        return StringUtils.join("/", StringUtils.join(args, ""));
    }
}
