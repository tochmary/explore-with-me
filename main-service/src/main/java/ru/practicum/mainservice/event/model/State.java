package ru.practicum.mainservice.event.model;

import java.util.Optional;

/**
 * Состояние жизненного цикла события
 */
public enum State {
    /**
     * Ожидание модерации
     */
    PENDING,
    /**
     * Опубликовано
     */
    PUBLISHED,
    /**
     * Отменено
     */
    CANCELED;

    public static Optional<State> toState(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}