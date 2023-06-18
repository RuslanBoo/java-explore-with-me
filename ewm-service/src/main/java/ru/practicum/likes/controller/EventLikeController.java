package ru.practicum.likes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.likes.enums.EventLikeType;
import ru.practicum.likes.service.EventLikeService;
import ru.practicum.users.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/likes/{eventId}")
@RequiredArgsConstructor
public class EventLikeController {
    private final EventLikeService eventLikeService;

    @GetMapping
    public List<UserDto> getLikes(@PathVariable int eventId, @Valid @RequestParam EventLikeType type) {
        return eventLikeService.getEventLikeUsers(eventId, type);
    }
}
