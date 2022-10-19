package ru.practicum.mainservice.requests.mapper;

import ru.practicum.mainservice.requests.model.dto.ParticipationRequestDto;
import ru.practicum.mainservice.requests.model.entity.Request;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.common.Utility.checkForNull;

public class RequestMapper {

    public static ParticipationRequestDto toRequestDto(Request request) {
        checkForNull(request);
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setId(request.getId());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setStatus(request.getStatus());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setCreated(request.getCreated());
        return requestDto;
    }

    public static List<ParticipationRequestDto> getParticipationDtoList(List<Request> requestList) {
        return requestList.stream()
                .map(RequestMapper::toRequestDto)
                .sorted(Comparator.comparing(ParticipationRequestDto::getId))
                .collect(Collectors.toList());
    }
}