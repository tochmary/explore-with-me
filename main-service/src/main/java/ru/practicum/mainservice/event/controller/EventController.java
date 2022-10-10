package ru.practicum.mainservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.dto.EventFullDto;
import ru.practicum.mainservice.event.model.dto.EventShortDto;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    /**
     * Получение событий с возможностью фильтрации
     * это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
     * текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
     * если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
     * информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
     * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    список идентификаторов категорий в которых будет вестись поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param rangeStart    дата и время не раньше которых должно произойти событие
     * @param rangeEnd      дата и время не позже которых должно произойти событие
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие
     * @param sort          Вариант сортировки: по дате события или по количеству просмотров (EVENT_DATE, VIEWS)
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора
     * @param size          количество событий в наборе
     * @return List<EventShortDto> список Краткой информации о событии
     */
    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam String text,
                                             @RequestParam List<Long> categories,
                                             @RequestParam Boolean paid,
                                             @RequestParam String rangeStart,
                                             @RequestParam String rangeEnd,
                                             @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                             @RequestParam String sort,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение событий с возможностью фильтрации:");
        log.info("текст для поиска в содержимом аннотации и подробном описании события: {}", text);
        log.info("список идентификаторов категорий в которых будет вестись поиск: {}", categories);
        log.info("поиск только платных/бесплатных событий: {}", paid);
        log.info("дата и время не раньше которых должно произойти событие: {}", rangeStart);
        log.info("дата и время не позже которых должно произойти событие: {}", rangeEnd);
        log.info("только события у которых не исчерпан лимит запросов на участие: {}", onlyAvailable);
        log.info("Вариант сортировки: по дате события или по количеству просмотров (EVENT_DATE, VIEWS): {}", sort);
        log.info("количество событий, которые нужно пропустить для формирования текущего набора: {}", from);
        log.info("количество событий в наборе: {}", size);
        List<Event> eventList = eventService.getEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return EventMapper.getEventShortDtoList(eventList);
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     *
     * @param id id события
     * @return EventFullDto Полная информация о событии
     */
    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable long id) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору с id={}", id);
        Event event = eventService.getEventById(id);
        return EventMapper.toEventFullDto(event);
    }
}