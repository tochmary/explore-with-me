package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.common.exception.BadRequestException;
import ru.practicum.mainservice.common.exception.NotFoundException;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.model.entity.EventState;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.event.repository.EventStateRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventStateRepository eventStateRepository;
    private final EntityManager entityManager;

    @Override
    public List<Event> getEvents(List<Long> users,
                                 List<State> states,
                                 List<Long> categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 Integer from,
                                 Integer size) {
        log.debug("Получение списка событий:");
        log.info("users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        PageRequest pr = PageRequest.of(from / size, size);
        return eventRepository.getEvents(users, states,
                categories, rangeStart, rangeEnd, pr).toList();
    }

    @Override
    @Transactional
    public Event updateEvent(long eventId, Event event) {
        log.debug("Обновление события c eventId={}, данные для обновления {}", eventId, event);
        Event eventNew = getEventByEventId(eventId);
        if (event.getAnnotation() != null) {
            eventNew.setAnnotation(event.getAnnotation());
        }
        if (event.getCategory() != null) {
            eventNew.setCategory(event.getCategory());
        }
        if (event.getDescription() != null) {
            eventNew.setDescription(event.getDescription());
        }
        if (event.getEventDate() != null) {
            eventNew.setEventDate(event.getEventDate());
        }
        if (event.getLocationLat() != null) {
            eventNew.setLocationLat(event.getLocationLat());
        }
        if (event.getLocationLon() != null) {
            eventNew.setLocationLon(event.getLocationLon());
        }
        if (event.getPaid() != null) {
            eventNew.setPaid(event.getPaid());
        }
        if (event.getParticipantLimit() != null) {
            eventNew.setParticipantLimit(event.getParticipantLimit());
        }
        if (event.getRequestModeration() != null) {
            eventNew.setRequestModeration(event.getRequestModeration());
        }
        if (event.getTitle() != null) {
            eventNew.setTitle(event.getTitle());
        }
        return eventRepository.save(eventNew);
    }

    @Override
    @Transactional
    public Event publishEventById(long eventId) {
        log.info("Публикация события с eventId={}", eventId);
        Event event = getEventByEventId(eventId);
        if (EventMapper.getStateLast(event) != State.PENDING) {
            throw new BadRequestException("Событие должно быть в состоянии ожидания публикации!");
        }
        EventState eventState = saveState(event, State.PUBLISHED);
        LocalDateTime publishedOn = eventState.getCreatedOn();
        if (event.getEventDate().isBefore(publishedOn.minusHours(1))) {
            throw new BadRequestException("Дата начала события должна быть не ранее чем за час от даты публикации!");
        }
        event.setPublishedOn(publishedOn);
        eventRepository.save(event);
        entityManager.detach(event);
        return getEventByEventId(eventId);
    }

    @Override
    @Transactional
    public Event rejectEventById(long eventId) {
        log.info("Отклонение события с eventId={}", eventId);
        Event event = getEventByEventId(eventId);
        if (EventMapper.getStateLast(event) == State.PUBLISHED) {
            throw new BadRequestException("Событие не должно быть опубликовано!");
        }
        saveState(event, State.CANCELED);
        entityManager.detach(event);
        return getEventByEventId(eventId);
    }

    @Override
    public List<Event> getEvents(String text,
                                 List<Long> categories,
                                 Boolean paid,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
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
        Event eventNew = eventRepository.save(event);
        saveState(eventNew, State.PENDING);
        entityManager.detach(event);
        return getEventByEventId(event.getId());
    }

    private EventState saveState(Event event, State state) {
        EventState eventState = new EventState();
        eventState.setEvent(event);
        eventState.setState(state);
        eventStateRepository.save(eventState);
        return eventState;
    }

    @Override
    public Event getEvent(long userId, long eventId) {
        log.debug("Получение события с id={}", eventId);
        Event event = getEventByEventId(eventId);
        checkUserForEvent(userId, event);
        return event;
    }

    @Override
    @Transactional
    public Event cancelEvent(long userId, long eventId) {
        log.debug("Отмена события с id={}", eventId);
        Event event = getEventByEventId(eventId);
        checkUserForEvent(userId, event);
        saveState(event, State.CANCELED);
        entityManager.detach(event);
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

    @Override
    public List<Event> getEventsByCatId(long catId) {
        return eventRepository.getEventsByCategoryId(catId);
    }
}
