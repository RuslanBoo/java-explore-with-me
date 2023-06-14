package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.StatClient;
import ru.practicum.core.utils.DateTimeService;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.enums.EventSort;
import ru.practicum.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventControllerPublic {
    private final EventService eventService;
    private final StatClient statClient;
    private final String statAppName = "ewm-main-service";

    @GetMapping
    public List<EventShortDto> search(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<@Valid @Positive Integer> categories,
                                      @RequestParam(required = false) @Valid Boolean paid,
                                      @RequestParam(required = false) @Valid Boolean onlyAvailable,
                                      @RequestParam(required = false) @Valid EventSort sort,
                                      @RequestParam(required = false) @Valid @DateTimeFormat(pattern = DateTimeService.DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                      @RequestParam(required = false) @Valid @DateTimeFormat(pattern = DateTimeService.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest request) {
        List<EventShortDto> result = eventService.searchByUser(text, paid, onlyAvailable, categories, rangeStart, rangeEnd, sort, from, size);
        statClient.saveHit(statAppName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return result;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable int eventId, HttpServletRequest request) {
        EventFullDto result = eventService.publicFindById(eventId);
        statClient.saveHit(statAppName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return result;
    }
}
