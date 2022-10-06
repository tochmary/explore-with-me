package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.repository.EventRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public List<Event> getEvents(List<Integer> users,
                                 List<String> states,
                                 List<Integer> categories,
                                 String rangeStart,
                                 String rangeEnd,
                                 Integer from,
                                 Integer size) {
        log.debug("Получение списка событий:");
        log.info("users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        PageRequest pr = PageRequest.of(from / size, size);
        return eventRepository.getEvents(users, states, categories, rangeStart, rangeEnd, pr).toList();
    }
/*
    @Override
    @Transactional
    public Event addEvent(Event event) {
        log.debug("Добавление пользователя {}", event);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public void deleteEvent(long eventId) {
        log.debug("Удаление пользователя с eventId={}", eventId);
        eventRepository.deleteById(eventId);
    }

    @Override
    public void checkEvent(long eventId) {
        log.debug("Проверка существования пользователя с eventId={}", eventId);
        getEventByEventId(eventId);
    }

    Event getEventByEventId(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Пользователя с eventId=" + eventId + " не существует!")
        );
    }
    */
}
