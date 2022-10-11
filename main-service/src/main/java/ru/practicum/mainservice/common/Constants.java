package ru.practicum.mainservice.common;

import java.time.format.DateTimeFormatter;
import java.util.Set;

public class Constants {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String EVENT_DATE = "count(event_date) asc";
    public static final String VIEWS = "count(views) asc";

    public static final Set<String> SORTS = Set.of(EVENT_DATE, VIEWS);
}
