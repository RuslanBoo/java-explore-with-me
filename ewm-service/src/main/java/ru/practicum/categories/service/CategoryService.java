package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.categories.CategoryMapper;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.core.pagination.PaginationService;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.DataNotFoundException;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PaginationService paginationService;
    private final EventRepository eventRepository;

    private CategoryDto prepareDto(Category category) {
        return categoryMapper.toDto(category);
    }

    private Category findById(int categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isEmpty()) {
            throw new DataNotFoundException(Category.class.getName(), categoryId);
        }

        return category.get();
    }

    private void checkName(int categoryId, String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isPresent() && category.get().getId() != categoryId) {
            throw new ConflictException(String.format("Category name '%s' already exist", name));
        }
    }

    private Category partialUpdate(int categoryId, CategoryDto categoryDto) {
        Category category = findById(categoryId);
        categoryMapper.update(categoryDto, category);

        return category;
    }

    public CategoryDto save(CategoryDto categoryDto) {
        checkName(0, categoryDto.getName());
        Category newCategory = categoryRepository.save(categoryMapper.fromDto(categoryDto));

        return prepareDto(newCategory);
    }

    public CategoryDto update(int catId, CategoryDto categoryDto) {
        checkName(catId, categoryDto.getName());
        Category category = categoryRepository.save(partialUpdate(catId, categoryDto));

        return prepareDto(category);
    }

    public void remove(int catId) {
        Category deletedCategory = findById(catId);
        List<Event> events = eventRepository.findAllByCategoryId(catId);

        if (!events.isEmpty()) {
            throw new ConflictException("The category is not empty");
        }

        categoryRepository.delete(deletedCategory);
    }

    public List<CategoryDto> getAll(int from, int size) {
        Pageable pageable = paginationService.getPageable(from, size);

        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getById(int catId) {
        return prepareDto(findById(catId));
    }
}
