package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.event.mapper.EventMapper;
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

    /**
     * Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
     */
    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam List<Integer> users,
                                        @RequestParam List<String> states,
                                        @RequestParam List<Integer> categories,
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
}