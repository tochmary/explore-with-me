package ru.practicum.mainservice.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.dto.CategoryDto;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.category.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "20") Integer size) {
        log.info("Получение списка категорий для from={}, size={}", from, size);
        List<Category> categoryList = categoryService.getCategories(from, size);
        return CategoryMapper.getCategoryDtoList(categoryList);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.info("Получение категории с catId={}", catId);
        Category category = categoryService.getCategoryById(catId);
        return CategoryMapper.toCategoryDto(category);
    }

    @PostMapping("/admin/categories")
    public CategoryDto addCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Добавление категории {}", categoryDto);
        Category category = CategoryMapper.toCategory(categoryDto);
        category = categoryService.addCategory(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Добавление категории {} с catId={}", categoryDto);
        Category category = CategoryMapper.toCategory(categoryDto);
        category = categoryService.updateCategory(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        log.info("Удаление категории с catId={}", catId);
        categoryService.deleteCategory(catId);
    }
}