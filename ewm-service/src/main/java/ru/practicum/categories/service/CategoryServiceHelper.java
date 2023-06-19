package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.error.exception.DataNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceHelper {
    private final CategoryRepository categoryRepository;

    public Category findById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->  new DataNotFoundException(Category.class.getName(), id)
        );
    }
}
