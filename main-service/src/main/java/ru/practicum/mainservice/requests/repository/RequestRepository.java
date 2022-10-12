package ru.practicum.mainservice.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.requests.model.entity.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findRequestsByEventId(long eventId);

    List<Request> findRequestsByRequesterId(long userId);
}
