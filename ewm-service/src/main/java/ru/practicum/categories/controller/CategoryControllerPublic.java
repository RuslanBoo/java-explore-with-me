package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CategoryControllerPublic {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAll(@RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        List<CategoryDto> result = categoryService.getAll(from, size);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable int catId) {
        CategoryDto categoryDto = categoryService.getById(catId);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }
}
