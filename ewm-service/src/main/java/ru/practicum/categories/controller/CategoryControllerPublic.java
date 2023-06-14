package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CategoryControllerPublic {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getById(@PathVariable int catId) {
        return categoryService.getById(catId);
    }
}
