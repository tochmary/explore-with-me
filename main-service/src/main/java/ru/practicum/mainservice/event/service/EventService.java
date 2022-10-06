package ru.practicum.mainservice.event.service;

import ru.practicum.mainservice.event.model.entity.Event;

import java.util.List;

public interface EventService {
    List<Event> getEvents(List<Integer> users,
                          List<String> states,
                          List<Integer> categories,
                          String rangeStart,
                          String rangeEnd,
                          Integer from,
                          Integer size);
}