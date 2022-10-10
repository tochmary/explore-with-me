package ru.practicum.mainservice.common;

import java.util.Set;

public class Constants {
    public static final String EVENT_DATE = "count(event_date) asc";
    public static final String VIEWS = "count(views) asc";

    public static final Set<String> SORTS = Set.of(EVENT_DATE, VIEWS);
}
