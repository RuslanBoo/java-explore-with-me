package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.core.utils.DateTimeService;
import ru.practicum.users.dto.UserShortDto;

import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
public class EventShortDto {
    private int id;
    private String title;
    private String description;
    private String annotation;

    @JsonFormat(pattern = DateTimeService.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private CategoryDto category;
    private UserShortDto initiator;
    private boolean paid;
    private int confirmedRequests;
    private int views;
}
