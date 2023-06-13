package ru.practicum.compilation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
public class NewCompilationDto {

    @NotBlank(message = "Field: title. Error: must not be blank. Value: ${validatedValue}")
    @Size(min = 2, max = 50, message = "Field: title. Error: must not from {min} to {max}. Value: ${validatedValue}")
    private String title;
    private Set<Integer> events;
    private boolean pinned;
}
