package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.dto.AdminUpdateEventRequest;
import ru.practicum.mainservice.event.model.dto.EventFullDto;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService eventService;
    private final CategoryService categoryService;

    /**
     * Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
     * @param users список id пользователей, чьи события нужно найти
     * @param states список состояний в которых находятся искомые события
     * @param categories список id категорий в которых будет вестись поиск
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd дата и время не позже которых должно произойти событие
     * @param from количество событий, которые нужно пропустить для формирования текущего набора
     * @param size количество событий в наборе
     * @return EventFullDto Полная информация о событии
     */
    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam List<Long> users,
                                        @RequestParam List<String> states,
                                        @RequestParam List<Long> categories,
                                        @RequestParam String rangeStart,
                                        @RequestParam String rangeEnd,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                        @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поиск событий:");
        log.info("список id пользователей, чьи события нужно найти: {}", users);
        log.info("список состояний в которых находятся искомые события: {}", states);
        log.info("список id категорий в которых будет вестись поиск: {}", categories);
        log.info("дата и время не раньше которых должно произойти событие: {}", rangeStart);
        log.info("дата и время не позже которых должно произойти событие: {}", rangeEnd);
        log.info("количество событий, которые нужно пропустить для формирования текущего набора: {}", from);
        log.info("количество событий в наборе: {}", size);
        List<Event> eventList = eventService.getEvents(
                users, states, categories, rangeStart, rangeEnd, from, size);
        return EventMapper.getEventFullDtoList(eventList);
    }

    /**
     * Редактирование данных любого события администратором. Валидация данных не требуется.
     * @param eventId Идентификатор события
     * @param eventUpdateDto Информация для редактирования события администратором. Все поля необязательные. Значение полей не валидируется.
     * @return EventFullDto Полная информация о событии
     */
    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @RequestBody AdminUpdateEventRequest eventUpdateDto) {
        log.info("Обновление события {} с eventId={}", eventUpdateDto, eventId);
        Category category = categoryService.getCategoryById(eventUpdateDto.getCategory());
        Event event = EventMapper.toEvent(eventUpdateDto, eventId, category);
        event = eventService.updateEvent(event);
        return EventMapper.toEventFullDto(event);
    }

    /**
     * Публикация события:
     * дата начала события должна быть не ранее чем за час от даты публикации.
     * событие должно быть в состоянии ожидания публикации
     * @param eventId Идентификатор события
     * @return EventFullDto Полная информация о событии
     */
    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        log.info("Публикация события с eventId={}", eventId);
        Event event = eventService.publishEventById(eventId);
        return EventMapper.toEventFullDto(event);
    }

    /**
     * Отклонение события:
     * событие не должно быть опубликовано.
     * @param eventId Идентификатор события
     * @return EventFullDto Полная информация о событии
     */
    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        log.info("Отклонение события с eventId={}", eventId);
        Event event = eventService.rejectEventById(eventId);
        return EventMapper.toEventFullDto(event);
    }
}