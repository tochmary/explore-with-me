package ru.practicum.mainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.event.model.Location;

import java.time.LocalDateTime;

/**
 * Информация для редактирования события администратором. Все поля необязательные. Значение полей не валидируется.:
 * annotation - Краткое описание события;
 * category - id категории, к которой относится событие;
 * description - Полное описание события;
 * eventDate - Дата и время, на которые намечено событие. Дата и время указываются в формате \"yyyy-MM-dd HH:mm:ss\";
 * location - Широта и долгота места проведения события;
 * paid - Нужно ли оплачивать участие в событии;
 * participantLimit - Ограничение на количество участников. Значение 0 - означает отсутствие ограничения;
 * requestModeration - Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.;
 * title - Заголовок события;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateEventRequest {
    private String annotation;
    private Integer category;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Long participantLimit = 0L;
    private Boolean requestModeration = true;
    private String title;
}