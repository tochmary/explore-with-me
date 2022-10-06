package ru.practicum.mainservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.event.model.entity.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e " +
            "from Event as e " +
            "where e.eventDate between :range_start and :range_end " +
            "and e.initiator.id in :users " +
            "and e.category.id in :categories ")
    Page<Event> getEvents(List<Integer> users,
                          List<String> states,
                          List<Integer> categories,
                          @Param("range_start") String rangeStart,
                          @Param("range_end") String rangeEnd,
                          Pageable pageable);
}
