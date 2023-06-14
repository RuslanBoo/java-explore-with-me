package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryService;

import javax.validation.Valid;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class CategoryControllerAdmin {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto save(@PathVariable int catId, @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(catId, categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int catId) {
        categoryService.remove(catId);
    }
}
