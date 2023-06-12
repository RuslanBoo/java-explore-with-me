package ru.practicum.compilation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.events.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class CompilationDto {
    int id;

    @NotBlank(message = "Field: title. Error: must not be blank. Value: ${validatedValue}")
    @Size(min = 2, max = 50, message = "Field: title. Error: must not from {min} to {max}. Value: ${validatedValue}")
    String title;

    List<EventShortDto> events;
    boolean pinned;
}
