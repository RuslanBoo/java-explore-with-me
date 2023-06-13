package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.core.utils.DateTimeService;
import ru.practicum.core.validation.DateBefore.DateBefore;
import ru.practicum.events.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class NewEventDto {

    @Size(min = 3, max = 120, message = "Field: title. Error: must not from {min} to {max}. Value: ${validatedValue}")
    private String title;

    @NotBlank
    @Size(min = 20, max = 2000, message = "Field: annotation. Error: must not from {min} to {max}. Value: ${validatedValue}")
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000, message = "Field: description. Error: must not from {min} to {max}. Value: ${validatedValue}")
    private String description;

    @Positive
    private int category;

    @DateBefore(hours = 2, message = "Field: eventDate. Error: must be after {hours} hours. Value: ${validatedValue}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeService.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid;
    private Boolean requestModeration;
    private int participantLimit;
}
