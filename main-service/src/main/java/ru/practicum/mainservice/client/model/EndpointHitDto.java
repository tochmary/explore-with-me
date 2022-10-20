package ru.practicum.mainservice.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * id — Идентификатор записи;
 * app — Идентификатор сервиса для которого записывается информация;
 * uri - URI для которого был осуществлен запрос;
 * ip - IP-адрес пользователя, осуществившего запрос;
 * timestamp - Дата и время, когда был совершен запрос к эндпоинту (в формате \"yyyy-MM-dd HH:mm:ss\").
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
