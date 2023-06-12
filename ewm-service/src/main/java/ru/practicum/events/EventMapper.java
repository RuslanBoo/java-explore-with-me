package ru.practicum.events;

import org.mapstruct.*;
import ru.practicum.categories.service.CategoryServiceHelper;
import ru.practicum.events.dto.*;
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
