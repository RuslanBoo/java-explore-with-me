package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationControllerAdmin {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        CompilationDto result = compilationService.save(newCompilationDto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<String> delete(@PathVariable int compId) {
        compilationService.delete(compId);

        return new ResponseEntity<>("Подборка удалена", HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PathVariable int compId,
                                                 @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        CompilationDto result = compilationService.update(compId, updateCompilationRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
