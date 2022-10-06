package ru.practicum.mainservice.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.dto.CategoryDto;
import ru.practicum.mainservice.category.model.dto.NewCategoryDto;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.category.service.CategoryService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto addCategory(@RequestBody NewCategoryDto categoryDto) {
        log.info("Добавление категории {}", categoryDto);
        Category category = CategoryMapper.toCategory(categoryDto);
        category = categoryService.addCategory(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Изменение категории {}", categoryDto);
        Category category = CategoryMapper.toCategory(categoryDto);
        category = categoryService.updateCategory(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        log.info("Удаление категории с catId={}", catId);
        categoryService.deleteCategory(catId);
    }
}