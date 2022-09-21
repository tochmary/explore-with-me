package ru.practicum.statsservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsservice.mapper.StatsMapper;
import ru.practicum.statsservice.model.dto.EndpointHitDto;
import ru.practicum.statsservice.model.dto.ViewStatsDto;
import ru.practicum.statsservice.model.entity.EndpointHit;
import ru.practicum.statsservice.service.StatsService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats() {
        log.info("Получение статистики по посещениям");
        return statsService.getStats();
    }

    @PostMapping("/hit")
    public void save(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Сохранение информации о том, что к эндпоинту был запрос {}", endpointHitDto);
        EndpointHit endpointHit = StatsMapper.toEndpointHit(endpointHitDto);
        statsService.save(endpointHit);
    }
}