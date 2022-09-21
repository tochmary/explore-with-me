package ru.practicum.statsservice.service;

import ru.practicum.statsservice.model.dto.ViewStatsDto;
import ru.practicum.statsservice.model.entity.EndpointHit;

import java.util.List;

public interface StatsService {
    List<ViewStatsDto> getStats();

    void save(EndpointHit endpointHit);
}
