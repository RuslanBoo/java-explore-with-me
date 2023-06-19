package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.core.utils.DateTimeService;
import ru.practicum.events.enums.EventState;
import ru.practicum.events.model.Location;
import ru.practicum.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class EventFullDto {
    private int id;
    private String title;
    private String description;
    private String annotation;

    private EventState state;
    private CategoryDto category;
    private UserShortDto initiator;
    private Location location;

    @JsonFormat(pattern = DateTimeService.DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    @JsonFormat(pattern = DateTimeService.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;

    @JsonFormat(pattern = DateTimeService.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private int confirmedRequests;
    private int participantLimit;
    private long views;
    private int likes;
    private int dislikes;
    private float rating;
    private boolean paid;
    private boolean requestModeration;
}
