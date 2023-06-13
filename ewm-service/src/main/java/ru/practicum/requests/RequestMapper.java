package ru.practicum.requests;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.categories.service.CategoryServiceHelper;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.Request;

@Mapper(componentModel = "spring", uses = {CategoryServiceHelper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RequestMapper {
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toShortDto(Request request);
}
