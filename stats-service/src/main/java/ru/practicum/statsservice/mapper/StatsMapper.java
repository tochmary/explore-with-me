package ru.practicum.statsservice.mapper;

import ru.practicum.statsservice.model.dto.EndpointHitDto;
import ru.practicum.statsservice.model.entity.EndpointHit;

import static ru.practicum.statsservice.common.Utility.checkForNull;

public class StatsMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        checkForNull(endpointHitDto, "endpointHitDto");
        return new EndpointHit(null,
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp()
        );
    }
}