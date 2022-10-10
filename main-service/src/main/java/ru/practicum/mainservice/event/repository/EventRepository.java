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
            //"join fetch e.state as s" +
            "where e.eventDate between :range_start and :range_end " +
            "and e.initiator.id in :users " +
            "and e.category.id in :categories")
    Page<Event> getEvents(@Param("users") List<Long> users,
                          //@Param("states") List<String> states,
                          @Param("categories") List<Long> categories,
                          @Param("range_start") String rangeStart,
                          @Param("range_end") String rangeEnd,
                          Pageable pageable);

    @Query("select e " +
            "from Event as e " +
            "where 1=1 " +
            "and (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%'))) " +
            "and e.category.id in :categories " +
            "and e.paid = :paid " +
            "and e.eventDate between :range_start and :range_end " +
            //onlyAvailable
            "order by :sort")
    Page<Event> getEvents(@Param("text") String text,
                          @Param("categories") List<Long> categories,
                          @Param("paid") Boolean paid,
                          @Param("range_start") String rangeStart,
                          @Param("range_end") String rangeEnd,
                          //@Param("only_available") Boolean onlyAvailable,
                          @Param("sort") String sort,
                          Pageable pageable);

    Page<Event> getEventsByInitiatorId(@Param("userID") long userID, Pageable pageable);
}
