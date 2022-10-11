package ru.practicum.mainservice.event.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.user.model.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Полная информация о событии:
 * id - Идентификатор;
 * annotation - Краткое описание;
 * category - Категория;
 * //* confirmedRequests - Количество одобренных заявок на участие в данном событии;
 * createdOn - Дата и время создания события (в формате \"yyyy-MM-dd HH:mm:ss\";
 * description - Полное описание события;
 * eventDate - Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\");
 * initiator - Пользователь (краткая информация);
 * location - Широта и долгота места проведения события;
 * paid - Нужно ли оплачивать участие;
 * participantLimit - Ограничение на количество участников. Значение 0 - означает отсутствие ограничения;
 * //*publishedOn - Дата и время публикации события (в формате \"yyyy-MM-dd HH:mm:ss\");
 * requestModeration - Нужна ли пре-модерация заявок на участие;
 * //* state - Список состояний жизненного цикла события;
 * title - Заголовок;
 * //* views - Количество просмотрев события;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    //private Integer confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "initiator_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User initiator;

    @Column(name = "location_lat")
    private Float locationLat;

    @Column(name = "location_lon")
    private Float locationLon;

    @Column
    private Boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit = 0L;

    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration = true;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<EventState> state;

    @Column
    private String title;

    //private Long views;
}
