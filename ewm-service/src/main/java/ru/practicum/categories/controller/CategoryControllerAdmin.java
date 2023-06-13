package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<CategoryDto> save(@RequestBody @Valid CategoryDto categoryDto) {
        CategoryDto newCategoryDto = categoryService.save(categoryDto);
        return new ResponseEntity<>(newCategoryDto, HttpStatus.CREATED);
    }

    @PatchMapping("/admin/categories/{catId}")
    public ResponseEntity<CategoryDto> save(@PathVariable int catId, @RequestBody @Valid CategoryDto categoryDto) {
        CategoryDto updatedCategoryDto = categoryService.update(catId, categoryDto);

        return new ResponseEntity<>(updatedCategoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public ResponseEntity<String> delete(@PathVariable int catId) {
        categoryService.remove(catId);

        return new ResponseEntity<>("Категория удалена", HttpStatus.NO_CONTENT);
    }
}
