package ru.practicum.events.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.events.enums.EventSort;
import ru.practicum.events.enums.EventState;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.QEvent;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public List<Event> findAllByAdmin(List<Integer> userIds, List<EventState> states, List<Integer> categoryIds, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        QEvent event = QEvent.event;
        BooleanExpression where = Expressions.asBoolean(true).isTrue();

        if (rangeStart != null) {
            where = where.and(event.eventDate.after(rangeStart));
        }

        if (rangeEnd != null) {
            where = where.and(event.eventDate.before(rangeEnd));
        }

        if (userIds != null && !userIds.isEmpty() && !userIds.equals(List.of(0))) {
            where = where.and(event.initiator.id.in(userIds));
        }

        if (states != null && !states.isEmpty()) {
            where = where.and(event.state.in(states));
        }

        if (categoryIds != null && !categoryIds.isEmpty() && !categoryIds.equals(List.of(0))) {
            where = where.and(event.category.id.in(categoryIds));
        }

        return new JPAQuery<Event>(entityManager)
                .from(event)
                .where(where)
                .offset(from)
                .limit(size)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findAllByUser(String text, Boolean onlyAvailable, Boolean paid, List<Integer> categoryIds, LocalDateTime rangeStart, LocalDateTime rangeEnd, EventSort sort, int from, int size) {
        QEvent event = QEvent.event;
        BooleanExpression where = Expressions.asBoolean(true).isTrue();

        if (text != null && !text.isBlank()) {
            where = where.and(event.annotation.containsIgnoreCase(text).or(event.description.containsIgnoreCase(text)));
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {
            where = where.and(event.category.id.in(categoryIds));
        }

        if (rangeStart == null && rangeEnd == null) {
            where = where.and(event.eventDate.after(LocalDateTime.now()));
        }

        if (rangeStart != null) {
            where = where.and(event.eventDate.after(rangeStart));
        }

        if (rangeEnd != null) {
            where = where.and(event.eventDate.before(rangeEnd));
        }

        if (paid != null) {
            where = where.and(event.paid.eq(paid));
        }

        if (onlyAvailable != null && onlyAvailable) {
            where = where.and(event.participantLimit.subtract(event.confirmedRequests).gt(0));
        }

        OrderSpecifier orderBy = event.id.asc();

        if (sort == EventSort.EVENT_DATE) {
            orderBy = event.eventDate.desc();
        }

        return new JPAQuery<Event>(entityManager)
                .from(event)
                .where(where)
                .offset(from)
                .orderBy(orderBy)
                .limit(size)
                .stream()
                .collect(Collectors.toList());
    }
}
