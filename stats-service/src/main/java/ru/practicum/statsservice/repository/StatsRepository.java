package ru.practicum.statsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.statsservice.model.dto.ViewStatsDto;
import ru.practicum.statsservice.model.entity.EndpointHit;

import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.statsservice.model.dto.ViewStatsDto(eh.app, eh.uri, count(eh)) " +
            "from EndpointHit as eh " +
            "group by eh.app, eh.uri")
    List<ViewStatsDto> getStats();

    @Query("select count(eh) " +
            "from EndpointHit as eh " +
            "where eh.uri = concat('/events/', :eventId) " +
            "group by eh.uri")
    Integer findCountStatsEvent(@Param("eventId") long eventId);
}
