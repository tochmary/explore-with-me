package ru.practicum.mainservice.requests.controller.privateC;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.requests.mapper.RequestMapper;
import ru.practicum.mainservice.requests.model.dto.ParticipationRequestDto;
import ru.practicum.mainservice.requests.model.entity.Request;
import ru.practicum.mainservice.requests.service.RequestService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class RequestPrivateController {
    private final RequestService requestService;

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return ParticipationRequestDto Заявка на участие в событии
     */
    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipants(
            @PathVariable long userId,
            @PathVariable long eventId) {
        log.info("Получение информации о запросах на участие в событии текущего пользователя:");
        log.info("id текущего пользователя: {}", userId);
        log.info("id события: {}", eventId);
        List<Request> requestList = requestService.getEventParticipants(userId, eventId);
        return RequestMapper.getParticipationDtoList(requestList);
    }

    /**
     * Подтверждение чужой заявки на участие в событии текущего пользователя
     *
     * @param userId  id текущего пользователя
     * @param eventId id события текущего пользователя
     * @param reqId   id заявки, которую подтверждает текущий пользователь
     * @return ParticipationRequestDto Заявка на участие в событии
     */
    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmParticipationRequest(
            @PathVariable long userId,
            @PathVariable long eventId,
            @PathVariable long reqId) {
        log.info("Подтверждение чужой заявки на участие в событии текущего пользователя:");
        log.info("id текущего пользователя: {}", userId);
        log.info("id события текущего пользователя: {}", eventId);
        log.info("id заявки, которую подтверждает текущий пользователь: {}", reqId);
        Request request = requestService.confirmParticipationRequest(userId, eventId, reqId);
        return RequestMapper.toRequestDto(request);
    }

    /**
     * Отклонение чужой заявки на участие в событии текущего пользователя
     *
     * @param userId  id текущего пользователя
     * @param eventId id события текущего пользователя
     * @param reqId   id заявки, которую отменяет текущий пользователь
     * @return ParticipationRequestDto Заявка на участие в событии
     */
    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable long userId,
            @PathVariable long eventId,
            @PathVariable long reqId) {
        log.info("Отклонение чужой заявки на участие в событии текущего пользователя:");
        log.info("id текущего пользователя: {}", userId);
        log.info("id события текущего пользователя: {}", eventId);
        log.info("id заявки, которую отменяет текущий пользователь: {}", reqId);
        Request request = requestService.cancelParticipationRequest(userId, eventId, reqId);
        return RequestMapper.toRequestDto(request);
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     *
     * @param userId id текущего пользователя
     * @return List<ParticipationRequestDto> Заявки на участие в событии
     */
    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(
            @PathVariable long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях:");
        log.info("id текущего пользователя: {}", userId);
        List<Request> requestList = requestService.getUserRequests(userId);
        return RequestMapper.getParticipationDtoList(requestList);
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     * нельзя добавить повторный запрос
     * - инициатор события не может добавить запрос на участие в своём событии
     * - нельзя участвовать в неопубликованном событии
     * - если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
     * - если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return ParticipationRequestDto Заявка на участие в событии
     */
    @PostMapping("/requests")
    public ParticipationRequestDto addParticipationRequest(
            @PathVariable long userId,
            @RequestParam long eventId) {
        log.info("Добавление запроса от текущего пользователя на участие в событии:");
        log.info("id текущего пользователя: {}", userId);
        log.info("id события: {}", eventId);
        Request request = requestService.addParticipationRequest(userId, eventId);
        return RequestMapper.toRequestDto(request);
    }

    /**
     * Отмена своего запроса на участие в событии
     *
     * @param userId    id текущего пользователя
     * @param requestId id запроса на участие
     * @return ParticipationRequestDto Заявка на участие в событии
     */
    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(
            @PathVariable long userId,
            @PathVariable long requestId) {
        log.info("Отмена своего запроса на участие в событии:");
        log.info("id текущего пользователя: {}", userId);
        log.info("id заявки, которую отменяет текущий пользователь: {}", requestId);
        Request request = requestService.cancelRequest(userId, requestId);
        return RequestMapper.toRequestDto(request);
    }
}