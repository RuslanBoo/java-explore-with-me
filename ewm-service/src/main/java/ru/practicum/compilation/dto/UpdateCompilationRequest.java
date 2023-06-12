package ru.practicum.compilation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
public class UpdateCompilationRequest {

    @Size(min = 2, max = 50, message = "Field: title. Error: must not from {min} to {max}. Value: ${validatedValue}")
    String title;
    boolean pinned;
    Set<Integer> events;
}
