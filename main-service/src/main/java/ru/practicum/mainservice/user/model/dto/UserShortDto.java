package ru.practicum.mainservice.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Пользователь (краткая информация):
 * id — идентификатор пользователя;
 * name — имя пользователя;
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    @NotBlank
    private Long id;

    @NotBlank
    private String name;
}
