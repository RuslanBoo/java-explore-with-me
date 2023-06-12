package ru.practicum.events.repository;

import ru.practicum.events.enums.EventSort;
import ru.practicum.events.enums.EventState;
import ru.practicum.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findAllByAdmin(
            List<Integer> userIds,
            List<EventState> states,
            List<Integer> categoryIds,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size
    );

    List<Event> findAllByUser(
            String text,
            Boolean onlyAvailable,
            Boolean paid,
            List<Integer> categoryIds,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            EventSort sort,
            int from,
            int size
    );
}
