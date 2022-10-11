package ru.practicum.mainservice.event.service;

import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.model.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<Event> getEvents(List<Long> users,
                          List<State> states,
                          List<Long> categories,
                          LocalDateTime rangeStart,
                          LocalDateTime rangeEnd,
                          Integer from,
                          Integer size);

    Event updateEvent(long eventId, Event event);

    Event publishEventById(long eventId);

    Event rejectEventById(long eventId);

    List<Event> getEvents(String text,
                          List<Long> categories,
                          Boolean paid,
                          LocalDateTime rangeStart,
                          LocalDateTime rangeEnd,
                          Boolean onlyAvailable,
                          String sort,
                          Integer from,
                          Integer size);

    Event getEventById(long id);

    List<Event> getEvents(long userId, Integer from, Integer size);

    Event addEvent(Event event);

    Event getEvent(long userId, long eventId);

    Event cancelEvent(long userId, long eventId);

    Event getEventByEventId(long eventId);

    void checkUserForEvent(long userId, Event event);
}