package ru.practicum.users.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@RequiredArgsConstructor
public class UserDto {
    int id;

    @NotBlank(message = "Field: name. Error: must not be blank. Value: ${validatedValue}")
    @Size(min = 2, max = 250, message = "Field: name. Error: must not from {min} to {max}. Value: ${validatedValue}")
    String name;

    @NotBlank(message = "Field: email. Error: must not be blank. Value: ${validatedValue}")
    @Size(min = 6, max = 254, message = "Field: email. Error: must not from {min} to {max}. Value: ${validatedValue}")
    @Email(message = "Field: name. Error: email address is invalid. Value: ${validatedValue}")
    String email;
}
