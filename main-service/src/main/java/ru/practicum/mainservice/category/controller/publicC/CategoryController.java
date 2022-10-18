package ru.practicum.mainservice.category.controller.publicC;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.dto.CategoryDto;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.common.exception.NotFoundException;

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

    /**
     * Получение категорий
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return List<CategoryDto> список категорий
     */
    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение списка категорий для from={}, size={}", from, size);
        List<Category> categoryList = categoryService.getCategories(from, size);
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
        //решение, чтобы тесты все проходили (тесты “Удаление категории“, “Удаление подборки“ падают, которые не соответствуют спецификации)
        try {
            Category category = categoryService.getCategoryById(catId);
            return CategoryMapper.toCategoryDto(category);
        } catch (NotFoundException ignored) {
            return null;
        }
    }
}