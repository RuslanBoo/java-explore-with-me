package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.core.utils.DateTimeService;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.enums.EventState;
import ru.practicum.events.service.EventService;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> search(@RequestParam(required = false) List<Integer> users,
                                                     @RequestParam(required = false) List<EventState> states,
                                                     @RequestParam(required = false) List<Integer> categories,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = DateTimeService.DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = DateTimeService.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        List<EventFullDto> result = eventService.searchByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> setStatus(@PathVariable int eventId,
                                                  @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        EventFullDto result = eventService.updateByAdmin(eventId, updateEventAdminRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
