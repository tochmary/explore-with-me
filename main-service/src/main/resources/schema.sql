DROP TABLE IF EXISTS categories, requests, compilation_events, event_states, events, users, compilations;

--Таблица категорий
/**
 * id — Идентификатор категории;
 * name — Название категории
 */
CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT categories_pk PRIMARY KEY (id)
);

--Таблица пользователей
/**
 * id — уникальный идентификатор пользователя;
 * name — имя или логин пользователя;
 * email — адрес электронной почты (учтите, что два пользователя не могут
 * иметь одинаковый адрес электронной почты).
 */
CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT users_email_uq UNIQUE (email)
);

--Таблица событий
/*
 * Событие::
 * id - Идентификатор;
 * annotation - Краткое описание;
 * category - Категория;
 --* confirmedRequests - Количество одобренных заявок на участие в данном событии;
 * createdOn - Дата и время создания события (в формате \"yyyy-MM-dd HH:mm:ss\";
 * description - Полное описание события;
 * eventDate - Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\");
 * initiator - Пользователь (краткая информация);
 * location - Широта и долгота места проведения события;
 * paid - Нужно ли оплачивать участие;
 * participantLimit - Ограничение на количество участников. Значение 0 - означает отсутствие ограничения;
 --* publishedOn - Дата и время публикации события (в формате \"yyyy-MM-dd HH:mm:ss\");
 * requestModeration - Нужна ли пре-модерация заявок на участие;
 --* state - Список состояний жизненного цикла события;
 * title - Заголовок;
 --* views - Количество просмотрев события;*/
CREATE TABLE IF NOT EXISTS events
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    category_id BIGINT NOT NULL,
    initiator_id BIGINT NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    location_lat FLOAT NOT NULL,
    location_lon FLOAT NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit BIGINT DEFAULT 0,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT TRUE,
    CONSTRAINT events_pk PRIMARY KEY (id),
    CONSTRAINT events_category_id_fk FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT events_users_id_fk FOREIGN KEY (initiator_id) REFERENCES users (id)
    );

--Таблица состояний события
CREATE TABLE IF NOT EXISTS event_states
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT,
    state VARCHAR(30) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    constraint event_states_PK primary key (id),
    constraint event_states_event_id_FK foreign key (event_id) references events
    );

--Таблица запросов на участие
/**
 * Заявка на участие в событии:
 * id - Идентификатор заявки
 * event_id - Идентификатор события
 * status - Статус заявки
 * requester_id - Идентификатор пользователя, отправившего заявку
 * created - Дата и время создания заявки
 */
CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    requester_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT requests_pk PRIMARY KEY (id),
    CONSTRAINT requests_event_id_fk FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT requests_requester_id_fk FOREIGN KEY (requester_id) REFERENCES users (id)
    );

--Таблица подборок
/**
 * Подборка событий:
 * id - Идентификатор
 * pinned - Закреплена ли подборка на главной странице сайта
 * title - Заголовок подборки
 */
CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(120) NOT NULL,
    CONSTRAINT compilations_pk PRIMARY KEY (id)
);

--Таблица связей подборки с событиями
CREATE TABLE IF NOT EXISTS compilation_events
(
    comp_id  BIGINT,
    event_id BIGINT,
    constraint compilation_events_PK primary key (comp_id, event_id),
    constraint compilation_events_comp_id_FK foreign key (comp_id) references compilations,
    constraint compilation_events_event_id_FK foreign key (event_id) references events
    );