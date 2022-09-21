package ru.practicum.statsservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * app — Название сервиса;
 * uri -  URI сервиса;
 * hits - Количество просмотров.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
