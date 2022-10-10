package ru.practicum.mainservice.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.dto.CategoryDto;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    //private final StatsClient statsClient;

    /**
     * Получение категорий
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @param request HttpServletRequest
     * @return List<CategoryDto> список категорий
     */
    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "20") Integer size,
                                           HttpServletRequest request) {
        log.info("Получение списка категорий для from={}, size={}", from, size);
        List<Category> categoryList = categoryService.getCategories(from, size);
        //statsClient.save(request);
        return CategoryMapper.getCategoryDtoList(categoryList);
    }

    /**
     * Получение информации о категории по её идентификатору
     *
     * @param catId id категории
     * @return CategoryDto Категория
     */
    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable long catId) {
        log.info("Получение категории с catId={}", catId);
        Category category = categoryService.getCategoryById(catId);
        return CategoryMapper.toCategoryDto(category);
    }
}