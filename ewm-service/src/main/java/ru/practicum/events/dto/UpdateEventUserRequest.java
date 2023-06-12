package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.core.utils.DateTimeService;
import ru.practicum.core.validation.DateBefore.DateBefore;
import ru.practicum.events.enums.UserStateAction;
import ru.practicum.events.model.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
public class UpdateEventUserRequest {

    @Size(min = 3, max = 120, message = "Field: title. Error: must not from {min} to {max}. Value: ${validatedValue}")
    private String title;

    @Size(min = 20, max = 7000, message = "Field: description. Error: must not from {min} to {max}. Value: ${validatedValue}")
    private String description;

    @Size(min = 20, max = 2000, message = "Field: annotation. Error: must not from {min} to {max}. Value: ${validatedValue}")
    private String annotation;

    private Integer category;

    @DateBefore(hours = 2, message = "Field: eventDate. Error: must be after {hours} hours. Value: ${validatedValue}")
    @JsonFormat(pattern = DateTimeService.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private UserStateAction stateAction;
    private Location location;

    private Boolean paid;
    private Boolean requestModeration;

    private Integer participantLimit;

}
