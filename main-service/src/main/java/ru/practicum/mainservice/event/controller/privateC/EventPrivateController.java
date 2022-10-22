package ru.practicum.mainservice.event.controller.privateC;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.dto.EventFullDto;
import ru.practicum.mainservice.event.model.dto.EventShortDto;
import ru.practicum.mainservice.event.model.dto.NewEventDto;
import ru.practicum.mainservice.event.model.dto.UpdateEventRequest;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.user.model.entity.User;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.mainservice.common.Utility.checkForNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;
    private final CategoryService categoryService;
    private final UserService userService;

    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param userId id текущего пользователя
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   количество элементов в наборе
     * @return EventShortDto
     */
    @GetMapping
    public List<EventShortDto> getEvents(
            @Positive @PathVariable Integer userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение событий, добавленных текущим пользователем:");
        log.info("id текущего пользователя: {}", userId);
        log.info("количество элементов, которые нужно пропустить для формирования текущего набора: {}", from);
        log.info("количество элементов в наборе: {}", size);
        List<Event> eventList = eventService.getEvents(userId, from, size);
        return EventMapper.getEventShortDtoList(eventList);
    }

    /**
     * Изменение события добавленного текущим пользователем
     * изменить можно только отмененные события или события в состоянии ожидания модерации
     * если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
     * дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     *
     * @param userId         id текущего пользователя
     * @param eventUpdateDto Новые данные события
     * @return EventFullDto Полная информация о событии
     */
    @PatchMapping
    public EventFullDto updateEvent(
            @Positive @PathVariable long userId,
            @RequestBody UpdateEventRequest eventUpdateDto
    ) {
        log.info("Изменение события добавленного текущим пользователем:");
        log.info("id текущего пользователя: {}", userId);
        log.info("Новые данные события: {}", eventUpdateDto);
        checkForNull(eventUpdateDto, "eventUpdateDto");
        User user = userService.getUserByUserId(userId);
        Category category = categoryService.getCategoryById(eventUpdateDto.getCategory());
        Event event = EventMapper.toEvent(eventUpdateDto, user, category);
        event = eventService.updateEvent(eventUpdateDto.getEventId(), event, true);
        return EventMapper.toEventFullDto(event);
    }

    /**
     * Добавление нового события
     * дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     *
     * @param userId      id текущего пользователя
     * @param newEventDto Новое событие
     * @return EventFullDto Полная информация о событии
     */
    @PostMapping
    public EventFullDto addEvent(
            @Positive @PathVariable long userId,
            @RequestBody NewEventDto newEventDto
    ) {
        log.info("Добавление нового события:");
        log.info("id текущего пользователя: {}", userId);
        log.info("Новое событие: {}", newEventDto);
        checkForNull(newEventDto, "newEventDto");
        User user = userService.getUserByUserId(userId);
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        Event event = EventMapper.toEvent(newEventDto, user, category);
        event = eventService.addEvent(event);
        return EventMapper.toEventFullDto(event);
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return EventFullDto Полная информация о событии
     */
    @GetMapping("/{eventId}")
    public EventFullDto getEvent(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        log.info("Получение полной информации о событии добавленном текущим пользователем:");
        log.info("id текущего пользователя: {}", userId);
        log.info("id события: {}", eventId);
        Event event = eventService.getEvent(userId, eventId);
        return EventMapper.toEventFullDto(event);
    }

    /**
     * Отмена события добавленного текущим пользователем
     * Отменить можно только событие в состоянии ожидания модерации
     *
     * @param userId  id текущего пользователя
     * @param eventId id отменяемого события
     * @return EventFullDto Полная информация о событии
     */
    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(
            @Positive @PathVariable Long userId,
            @Positive @PathVariable Long eventId) {
        log.info("Отмена события добавленного текущим пользователем:");
        log.info("id текущего пользователя: {}", userId);
        log.info("id отменяемого события: {}", eventId);
        Event event = eventService.cancelEvent(userId, eventId);
        return EventMapper.toEventFullDto(event);
    }
}