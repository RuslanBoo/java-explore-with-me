package ru.practicum.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @Transient
    private int id;

    @NotBlank(message = "Field: name. Error: must not be blank. Value: ${validatedValue}")
    @Size(min = 2, max = 50, message = "Field: name. Error: must not from {min} to {max}. Value: ${validatedValue}")
    private String name;
}
