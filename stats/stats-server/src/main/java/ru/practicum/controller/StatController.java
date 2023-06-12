package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping(path = "/hit")
    public ResponseEntity<String> saveHit(@Validated @RequestBody EndpointHitDto endpointHitDto) {
        return statService.saveHit(endpointHitDto);
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        if (end.isBefore(start)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return statService.getStats(start, end, uris, unique);
    }
}
