package ru.practicum.statsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsservice.model.dto.ViewStatsDto;
import ru.practicum.statsservice.model.entity.EndpointHit;
import ru.practicum.statsservice.repository.StatsRepository;

import java.util.List;

import static ru.practicum.statsservice.common.Utility.checkForNull;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public List<ViewStatsDto> getStats() {
        log.debug("Получение списка всех endpointHit");
        return statsRepository.getStats();
    }

    @Override
    @Transactional
    public void save(EndpointHit endpointHit) {
        log.debug("Добавление endpointHit {}", endpointHit);
        checkForNull(endpointHit, "endpointHitDto");
        statsRepository.save(endpointHit);
    }

    @Override
    public Integer getStatsEvent(long eventId) {
        log.debug("Получение количества просмотра события с eventId={}", eventId);
        return statsRepository.findCountStatsEvent(eventId);
    }
}
