package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.common.exception.NotFoundException;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.repository.EventRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public List<Event> getEvents(List<Long> users,
                                 List<String> states,
                                 List<Long> categories,
                                 String rangeStart,
                                 String rangeEnd,
                                 Integer from,
                                 Integer size) {
        log.debug("Получение списка событий:");
        log.info("users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        PageRequest pr = PageRequest.of(from / size, size);
        return eventRepository.getEvents(users, //states,
                categories, rangeStart, rangeEnd, pr).toList();
    }

    @Override
    @Transactional
    public Event updateEvent(Event event) {
        log.debug("Обновление события {}", event);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event publishEventById(long eventId) {
        log.info("Публикация события с eventId={}", eventId);
        Event event = getEventByEventId(eventId);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event rejectEventById(long eventId) {
        log.info("Отклонение события с eventId={}", eventId);
        Event event = getEventByEventId(eventId);
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getEvents(String text,
                                 List<Long> categories,
                                 Boolean paid,
                                 String rangeStart,
                                 String rangeEnd,
                                 Boolean onlyAvailable,
                                 String sort,
                                 Integer from,
                                 Integer size) {
        log.debug("Получение списка событий с возможностью фильтрации:");
        log.info("text={}, categories={}, states={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        PageRequest pr = PageRequest.of(from / size, size);
        return eventRepository.getEvents(text, categories, paid, rangeStart, rangeEnd, //onlyAvailable,
                sort, pr).toList();
    }

    @Override
    public Event getEventById(long id) {
        log.debug("Получение события с id={}", id);
        return getEventByEventId(id);
    }

    @Override
    public List<Event> getEvents(long userId, Integer from, Integer size) {
        log.debug("Получение списка событий, добавленных текущим пользователем:");
        log.info("userId={}, from={}, size={}", userId, from, size);
        PageRequest pr = PageRequest.of(from / size, size);
        return eventRepository.getEventsByInitiatorId(userId, pr).toList();
    }

    @Override
    @Transactional
    public Event addEvent(Event event) {
        log.debug("Добавление события {}", event);
        return eventRepository.save(event);
    }

    @Override
    public Event getEvent(long userId, long eventId) {
        log.debug("Получение события с id={}", eventId);
        Event event = getEventByEventId(eventId);
        checkUserForEvent(userId, event);
        return event;
    }

    @Override
    public Event cancelEvent(long userId, long eventId) {
        log.debug("Отмена события с id={}", eventId);
        Event event = getEventByEventId(eventId);
        checkUserForEvent(userId, event);
        //отмена события
        return eventRepository.save(event);
    }

    @Override
    public Event getEventByEventId(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Пользователя с eventId=" + eventId + " не существует!")
        );
    }

    @Override
    public void checkUserForEvent(long userId, Event event) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NotFoundException("У пользователя с userId=" + userId + " eventId=" + event.getId() + " не существует!");
        }
    }
}
