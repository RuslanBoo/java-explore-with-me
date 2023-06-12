package ru.practicum.categories;

import org.mapstruct.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category fromDto(CategoryDto categoryDto);

    @InheritConfiguration
    void update(CategoryDto categoryDto, @MappingTarget Category category);
}
