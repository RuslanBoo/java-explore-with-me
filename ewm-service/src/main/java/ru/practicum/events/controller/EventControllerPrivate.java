package ru.practicum.events.controller;

import javax.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.service.EventService;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.RequestService;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getByUserId(@PathVariable int userId,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<EventShortDto> result = eventService.getByUserId(userId, from, size);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> save(@PathVariable int userId,
                                             @Validated @RequestBody NewEventDto newEventDto) {
        EventFullDto result = eventService.save(userId, newEventDto);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getByEventId(@PathVariable int userId,
                                                     @PathVariable int eventId) {
        EventFullDto event = eventService.getByEventId(userId, eventId);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> update(@PathVariable int userId,
                                               @PathVariable int eventId,
                                               @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        EventFullDto result = eventService.update(userId, eventId, updateEventUserRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable int userId,
                                                                     @PathVariable int eventId) {
        List<ParticipationRequestDto> result = requestService.getByEventId(userId, eventId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequests(
            @PathVariable int userId,
            @PathVariable int eventId,
            @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        EventRequestStatusUpdateResult result = requestService.updateAllByEvent(userId, eventId, updateRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
