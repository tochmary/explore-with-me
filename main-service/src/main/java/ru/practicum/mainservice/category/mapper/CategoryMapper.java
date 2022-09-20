package ru.practicum.mainservice.category.mapper;

import ru.practicum.mainservice.category.model.dto.CategoryDto;
import ru.practicum.mainservice.category.model.entity.Category;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName()
        );
    }

    public static List<CategoryDto> getCategoryDtoList(List<Category> categoryList) {
        return categoryList.stream()
                .map(CategoryMapper::toCategoryDto)
                .sorted(Comparator.comparing(CategoryDto::getId))
                .collect(Collectors.toList());
    }
}