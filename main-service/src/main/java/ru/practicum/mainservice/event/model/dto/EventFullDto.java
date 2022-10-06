package ru.practicum.mainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.category.model.dto.CategoryDto;
import ru.practicum.mainservice.event.model.Location;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.user.model.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Полная информация о событии:
 * id - Идентификатор;
 * annotation - Краткое описание;
 * category - Категория;
 * confirmedRequests - Количество одобренных заявок на участие в данном событии;
 * createdOn - Дата и время создания события (в формате \"yyyy-MM-dd HH:mm:ss\";
 * description - Полное описание события;
 * eventDate - Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\");
 * initiator - Пользователь (краткая информация);
 * location - Широта и долгота места проведения события;
 * paid - Нужно ли оплачивать участие;
 * participantLimit - Ограничение на количество участников. Значение 0 - означает отсутствие ограничения;
 * publishedOn - Дата и время публикации события (в формате \"yyyy-MM-dd HH:mm:ss\");
 * requestModeration - Нужна ли пре-модерация заявок на участие;
 * state - Список состояний жизненного цикла события;
 * title - Заголовок;
 * views - Количество просмотрев события;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;

    @NotBlank
    private String annotation;

    @NotBlank
    private CategoryDto category;

    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdOn;

    private String description;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime eventDate;

    @NotBlank
    private UserShortDto initiator;

    @NotBlank
    private Location location;

    @NotBlank
    private Boolean paid;

    private Long participantLimit = 0L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime publishedOn;

    private Boolean requestModeration = true;

    private List<State> state;

    @NotBlank
    private String title;

    private Long views;
}