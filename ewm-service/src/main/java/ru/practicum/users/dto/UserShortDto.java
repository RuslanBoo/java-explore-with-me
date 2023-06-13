package ru.practicum.users.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserShortDto {
    private int id;

    @NotBlank(message = "Field: name. Error: must be not blank. Value: ${validatedValue}")
    @Size(min = 2, max = 250, message = "Field: name. Error: must not from {min} to {max}. Value: ${validatedValue}")
    private String name;
}
