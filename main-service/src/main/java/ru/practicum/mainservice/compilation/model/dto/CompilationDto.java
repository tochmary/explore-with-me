package ru.practicum.mainservice.compilation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.event.model.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
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
public class CompilationDto {
    @NotBlank
    private Long id;

    private List<EventShortDto> events;

    @NotBlank
    private Boolean pinned;

    @NotBlank
    private String title;
}
