package ru.practicum.mainservice.event.mapper;

import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.event.model.Location;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.model.dto.*;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.model.entity.EventState;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, User user, Category category) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLocationLat(newEventDto.getLocation().getLat());
        event.setLocationLon(newEventDto.getLocation().getLon());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setInitiator(user);
        return event;
    }

    public static Event toEvent(UpdateEventRequest updateEvent,
                                User user,
                                Category category) {
        Event event = new Event();
        event.setId(updateEvent.getEventId());
        event.setAnnotation(updateEvent.getAnnotation());
        event.setCategory(category);
        event.setDescription(updateEvent.getDescription());
        event.setEventDate(updateEvent.getEventDate());
        Location location = updateEvent.getLocation();
        if (location != null) {
            event.setLocationLat(updateEvent.getLocation().getLat());
            event.setLocationLon(updateEvent.getLocation().getLon());
        }
        event.setPaid(updateEvent.getPaid());
        event.setParticipantLimit(updateEvent.getParticipantLimit());
        event.setRequestModeration(updateEvent.getRequestModeration());
        event.setTitle(updateEvent.getTitle());
        event.setInitiator(user);
        return event;
    }

    public static Event toEvent(AdminUpdateEventRequest eventUpdateDto,
                                long eventId,
                                Category category) {
        Event event = new Event();
        event.setId(eventId);
        event.setAnnotation(eventUpdateDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventUpdateDto.getDescription());
        event.setEventDate(eventUpdateDto.getEventDate());
        Location location = eventUpdateDto.getLocation();
        if (location != null) {
            event.setLocationLat(eventUpdateDto.getLocation().getLat());
            event.setLocationLon(eventUpdateDto.getLocation().getLon());
        }
        event.setPaid(eventUpdateDto.getPaid());
        event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        event.setRequestModeration(eventUpdateDto.getRequestModeration());
        event.setTitle(eventUpdateDto.getTitle());
        return event;
    }

    public static List<EventFullDto> getEventFullDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventFullDto)
                .sorted(Comparator.comparing(EventFullDto::getId))
                .collect(Collectors.toList());
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setLocation(LocationMapper.toLocation(event.getLocationLat(), event.getLocationLon()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(getStateLast(event));
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public static State getStateLast(Event event) {
        if (event.getState() != null && !event.getState().isEmpty()) {
            return event.getState().stream()
                    .max(Comparator.comparing(EventState::getId))
                    .orElseThrow()
                    .getState();
        }
        return null;
    }

    public static List<EventShortDto> getEventShortDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventShortDto)
                .sorted(Comparator.comparing(EventShortDto::getId))
                .collect(Collectors.toList());
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(CategoryMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());
        return eventShortDto;
    }
}