package ru.practicum.mainservice.category.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Категория:
 * id — Идентификатор категории;
 * name — Название категории
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @NotBlank
    private Long id;
    @NotBlank
    private String name;
}
