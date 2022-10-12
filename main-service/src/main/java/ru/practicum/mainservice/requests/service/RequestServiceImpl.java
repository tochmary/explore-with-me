package ru.practicum.mainservice.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.common.exception.NotFoundException;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.requests.model.entity.Request;
import ru.practicum.mainservice.requests.repository.RequestRepository;
import ru.practicum.mainservice.user.service.UserService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventService eventService;
    private final UserService userService;


    @Override
    public List<Request> getEventParticipants(long userId, long eventId) {
        log.debug("Получение информации о запросах на участие в событии с eventId={}, userId={}", eventId, userId);
        return requestRepository.findRequestsByRequesterIdAndEventId(userId, eventId);
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
        Request request = new Request();
        request.setEvent(eventService.getEventByEventId(eventId));
        request.setRequester(userService.getUserByUserId(userId));
        request.setStatus("PENDING");
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

    private void checkUserForRequest(long userId, Request request) {
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new NotFoundException("У пользователя с userId=" + userId +
                    " запроса на участие с requestId=" + request.getId() + " не существует!");
        }
    }

    private void checkEventForRequest(long eventId, Request request) {
        if (!Objects.equals(request.getEvent().getId(), eventId)) {
            throw new NotFoundException("Для события с eventId=" + eventId +
                    " запроса на участие с requestId=" + request.getId() + " не существует!");
        }
    }
}
