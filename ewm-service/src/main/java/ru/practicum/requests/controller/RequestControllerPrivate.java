package ru.practicum.requests.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.RequestService;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestControllerPrivate {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> get(@PathVariable int userId) {
        List<ParticipationRequestDto> result = requestService.getByUserId(userId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> add(@PathVariable @Valid int userId,
                                                       @RequestParam @Valid int eventId) {
        ParticipationRequestDto result = requestService.add(userId, eventId);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancel(@PathVariable int userId,
                                                          @PathVariable int requestId) {
        ParticipationRequestDto result = requestService.cancel(userId, requestId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
