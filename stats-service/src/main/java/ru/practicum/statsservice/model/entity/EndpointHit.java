package ru.practicum.statsservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * id — Идентификатор записи;
 * app — Идентификатор сервиса для которого записывается информация;
 * uri - URI для которого был осуществлен запрос;
 * ip - IP-адрес пользователя, осуществившего запрос;
 * timestamp - Дата и время, когда был совершен запрос к эндпоинту (в формате \"yyyy-MM-dd HH:mm:ss\").
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stats")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String app;
    @Column
    private String uri;
    @Column
    private String ip;
    @Column(name = "created")
    private String timestamp;
}
