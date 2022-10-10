package ru.practicum.mainservice.compilation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.event.model.entity.Event;

import javax.persistence.*;
import java.util.List;

/**
 * Подборка событий:
 * id - Идентификатор
 * events - Список событий входящих в подборку
 * pinned - Закреплена ли подборка на главной странице сайта
 * title - Заголовок подборки
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "compilation_events",
            joinColumns = @JoinColumn(name = "comp_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;

    @Column
    private Boolean pinned;

    @Column
    private String title;
}
