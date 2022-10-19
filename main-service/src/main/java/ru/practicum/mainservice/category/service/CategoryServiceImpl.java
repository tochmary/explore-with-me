package ru.practicum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.common.exception.BadRequestException;
import ru.practicum.mainservice.common.exception.NotFoundException;
import ru.practicum.mainservice.event.service.EventService;

import java.util.List;

import static ru.practicum.mainservice.common.Utility.checkForNull;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventService eventService;

    @Override
    public List<Category> getCategories(Integer from, Integer size) {
        log.debug("Получение списка всех категорий. Со страницы {} в количестве {}", from, size);
        PageRequest pr = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pr).toList();
    }

    @Override
    public Category getCategoryById(long catId) {
        log.debug("Получение категории с catId={}", catId);
        return getCategoryByCatId(catId);
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        log.debug("Добавление категории {}", category);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        checkForNull(category, "category");
        Long catId = category.getId();
        log.debug("Обновление категории {} с catId={}", category, catId);
        Category categoryNew = getCategoryByCatId(catId);
        if (category.getName() != null) {
            categoryNew.setName(category.getName());
        }
        return categoryRepository.save(categoryNew);
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        log.debug("Удаление категории с catId={}", catId);
        if (!eventService.getEventsByCatId(catId).isEmpty()) {
            throw new BadRequestException("С категорией не должно быть связано ни одного события!");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public void checkCategory(long catId) {
        log.debug("Проверка существования категории с catId={}", catId);
        getCategoryByCatId(catId);
    }

    Category getCategoryByCatId(long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Категории с catId=" + catId + " не существует!")
        );
    }
}
