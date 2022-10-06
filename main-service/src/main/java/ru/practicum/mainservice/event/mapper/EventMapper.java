package ru.practicum.mainservice.event.mapper;

import ru.practicum.mainservice.event.model.dto.EventFullDto;
import ru.practicum.mainservice.event.model.entity.Event;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

    public static EventFullDto toEventDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        return eventFullDto;
    }

    public static Event toEvent(EventFullDto eventFullDto) {
        Event event = new Event();
        event.setId(event.getId());
        return event;
    }

    public static List<EventFullDto> getEventFullDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventDto)
                .sorted(Comparator.comparing(EventFullDto::getId))
                .collect(Collectors.toList());
    }
}