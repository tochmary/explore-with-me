package ru.practicum.mainservice.requests.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Заявка на участие в событии:
 * id - Идентификатор заявки
 * eventId - Идентификатор события
 * status - Статус заявки
 * requesterId - Идентификатор пользователя, отправившего заявку
 * created - Дата и время создания заявки
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column
    private String status;

    @Column(name = "requester_id")
    private Long requesterId;

    @Column
    private LocalDateTime created;
}