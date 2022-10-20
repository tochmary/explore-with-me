package ru.practicum.mainservice.requests.service;

import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.requests.model.entity.Request;

import java.util.List;

public interface RequestService {
    List<Request> getEventParticipants(long userId, long eventId);

    Request confirmParticipationRequest(long userId, long eventId, long reqId);

    Request cancelParticipationRequest(long userId, long eventId, long reqId);

    List<Request> getUserRequests(long userId);

    Request addParticipationRequest(long userId, long eventId);

    Request cancelRequest(long userId, long requestId);

    Request getRequestById(long requestId);

    List<Request> getRequestsByEventId(long eventId);

    boolean isParticipantLimit(Event event);

    List<Request> getRequestsByEventIdAndStatus(long eventId, String status);
}
