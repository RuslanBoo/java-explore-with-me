package ru.practicum.events;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.categories.service.CategoryServiceHelper;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.model.Event;

@Mapper(componentModel = "spring", uses = {CategoryServiceHelper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "longitude", source = "location.lon")
    @Mapping(target = "latitude", source = "location.lat")
    Event fromNewDto(NewEventDto newEventDto);

    EventShortDto toShortDto(Event event);

    @Mapping(target = "location.lon", source = "longitude")
    @Mapping(target = "location.lat", source = "latitude")
    EventFullDto toFullDto(Event event);

    @InheritConfiguration
    @Mapping(target = "category.id", source = "category")
    void update(UpdateEventUserRequest updateEventUserRequest, @MappingTarget Event event);

    @InheritConfiguration
    @Mapping(target = "category.id", source = "category")
    void update(UpdateEventAdminRequest updateEventAdminRequest, @MappingTarget Event event);
}
