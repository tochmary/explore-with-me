package ru.practicum.mainservice.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.common.exception.BadRequestException;
import ru.practicum.mainservice.common.exception.NotFoundException;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.requests.model.entity.Request;
import ru.practicum.mainservice.requests.repository.RequestRepository;
import ru.practicum.mainservice.user.service.UserService;

import java.util.List;
import java.util.Objects;

import static ru.practicum.mainservice.common.Utility.checkForNull;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final EventService eventService;


    @Override
    public List<Request> getEventParticipants(long userId, long eventId) {
        log.debug("Получение информации о запросах на участие в событии с eventId (событие пользователя) ={}, userId={}",
                eventId, userId);
        eventService.checkUserForEvent(userId, eventService.getEventByEventId(eventId));
        return requestRepository.findRequestsByEventId(eventId);
    }

    @Override
    @Transactional
    public Request confirmParticipationRequest(long userId, long eventId, long reqId) {
        log.debug("Подтверждение чужой заявки на участие c reqId = {} в событии с eventId={} " +
                "текущего пользователя c userId={}", reqId, userId, eventId);
        Event event = eventService.getEventByEventId(eventId);
        eventService.checkUserForEvent(userId, event);
        Request request = getRequestById(reqId);
        checkEventForRequest(eventId, request);
        request.setStatus("CONFIRMED");
        return requestRepository.save(request);
    }

    @Override
    @Transactional
    public Request cancelParticipationRequest(long userId, long eventId, long reqId) {
        log.debug("Отклонение чужой заявки на участие c reqId = {} в событии с eventId={} " +
                "текущего пользователя c userId={}", reqId, userId, eventId);
        Event event = eventService.getEventByEventId(eventId);
        eventService.checkUserForEvent(userId, event);
        Request request = getRequestById(reqId);
        checkEventForRequest(eventId, request);
        request.setStatus("REJECTED");
        return requestRepository.save(request);
    }

    @Override
    public List<Request> getUserRequests(long userId) {
        log.debug("Получение списка заявок текущего пользователя c userId={} на участие в чужих событиях",
                userId);
        return requestRepository.findRequestsByRequesterId(userId);
    }

    @Override
    @Transactional
    public Request addParticipationRequest(long userId, long eventId) {
        log.debug("Добавление запроса от текущего пользователя с userId={} на участие в событии с eventId={}",
                userId, eventId);
        Event event = eventService.getEventByEventId(eventId);
        if (userId == event.getInitiator().getId()) {
            throw new BadRequestException("Инициатор события не может добавить запрос на участие в своём событии!");
        }
        if (EventMapper.getStateLast(event) != State.PUBLISHED) {
            throw new BadRequestException("Нельзя участвовать в неопубликованном событии!");
        }
        if (isParticipantLimit(event)) {
            throw new BadRequestException("Достигнут лимит запросов на участие (" + event.getParticipantLimit() + ") !");
        }
        Request request = new Request();
        request.setEvent(eventService.getEventByEventId(eventId));
        request.setRequester(userService.getUserByUserId(userId));
        if (!event.getRequestModeration()) {
            request.setStatus("CONFIRMED");
        } else {
            request.setStatus("PENDING");
        }
        return requestRepository.save(request);
    }

    @Override
    @Transactional
    public Request cancelRequest(long userId, long requestId) {
        log.debug("Отмена (userId={}) своего запроса на участие в событии с requestId={}", userId, requestId);
        Request request = getRequestById(requestId);
        checkUserForRequest(userId, request);
        request.setStatus("CANCELED");
        return requestRepository.save(request);
    }

    @Override
    public Request getRequestById(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запроса на участие с requestId=" + requestId + " не существует!")
        );
    }

    @Override
    public List<Request> getRequestsByEventId(long eventId) {
        return requestRepository.findRequestsByEventId(eventId);
    }

    @Override
    public List<Request> getRequestsByEventIdAndStatus(long eventId, String status) {
        return requestRepository.findRequestsByEventIdAndStatus(eventId, status);
    }

    @Override
    public boolean isParticipantLimit(Event event) {
        checkForNull(event, "event");
        return event.getParticipantLimit() != 0
                && event.getParticipantLimit() == getRequestsByEventId(event.getId()).size();
    }

    private void checkUserForRequest(long userId, Request request) {
        checkForNull(request, "request");
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new NotFoundException("У пользователя с userId=" + userId +
                    " запроса на участие с requestId=" + request.getId() + " не существует!");
        }
    }

    private void checkEventForRequest(long eventId, Request request) {
        checkForNull(request, "request");
        if (!Objects.equals(request.getEvent().getId(), eventId)) {
            throw new NotFoundException("Для события с eventId=" + eventId +
                    " запроса на участие с requestId=" + request.getId() + " не существует!");
        }
    }
}
