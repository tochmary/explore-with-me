package ru.practicum.mainservice.requests.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.user.model.entity.User;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event event;

    @Column
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User requester;

    @Column
    private LocalDateTime created = LocalDateTime.now();
}