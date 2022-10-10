package ru.practicum.mainservice.compilation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column
    private Boolean pinned;

    @Column
    private String title;
}
