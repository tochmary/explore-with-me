package ru.practicum.mainservice.category.service;

import ru.practicum.mainservice.category.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories(Integer from, Integer size);

    Category getCategoryById(Long catId);

    Category addCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(long catId);

    void checkCategory(long catId);
}
