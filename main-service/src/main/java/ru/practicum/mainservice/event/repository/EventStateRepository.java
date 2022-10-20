package ru.practicum.mainservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.event.model.entity.EventState;

public interface EventStateRepository extends JpaRepository<EventState, Long> {
}
