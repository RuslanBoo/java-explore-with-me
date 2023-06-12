package ru.practicum.compilation;

import org.mapstruct.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompilationMapper {
    @Mapping(target = "events", ignore = true)
    CompilationDto toDto(Compilation compilation);

    @Mapping(target = "events", ignore = true)
    Compilation fromDto(NewCompilationDto newCompilationDto);

    @InheritConfiguration
    @Mapping(target = "events", ignore = true)
    void update(UpdateCompilationRequest updateCompilationRequest, @MappingTarget Compilation compilation);
}
