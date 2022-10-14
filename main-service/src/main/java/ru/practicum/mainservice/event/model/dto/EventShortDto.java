package ru.practicum.mainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.category.model.dto.CategoryDto;
import ru.practicum.mainservice.user.model.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Краткая информация о событии:
 * id - Идентификатор;
 * annotation - Краткое описание;
 * category - Категория;
 * confirmedRequests - Количество одобренных заявок на участие в данном событии;
 * eventDate - Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\");
 * initiator - Пользователь (краткая информация);
 * paid - Нужно ли оплачивать участие;
 * title - Заголовок;
 * views - Количество просмотрев события;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Long id;

    @NotBlank
    private String annotation;

    @NotBlank
    private CategoryDto category;

    private Long confirmedRequests;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime eventDate;

    @NotBlank
    private UserShortDto initiator;

    @NotBlank
    private Boolean paid;

    @NotBlank
    private String title;

    private Long views;
}
