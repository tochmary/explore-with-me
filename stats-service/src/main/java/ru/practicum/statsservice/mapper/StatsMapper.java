package ru.practicum.statsservice.mapper;

import ru.practicum.statsservice.model.dto.EndpointHitDto;
import ru.practicum.statsservice.model.entity.EndpointHit;

public class StatsMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(null,
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp()
        );
    }
}