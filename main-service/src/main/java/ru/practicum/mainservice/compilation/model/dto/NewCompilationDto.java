package ru.practicum.mainservice.compilation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Новая подборка событий:
 * events - Список идентификаторов событий входящих в подборку
 * pinned - Закреплена ли подборка на главной странице сайта
 * title - Заголовок подборки
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private List<Long> events;

    private Boolean pinned = false;

    @NotBlank
    private String title;
}

