package ru.practicum.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.events.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event>, EventRepositoryCustom {
    Set<Event> findAllByIdIn(Set<Integer> id);

    List<Event> findAllByInitiatorId(int initiatorId, Pageable pageable);

    List<Event> findAllByCategoryId(int categoryId);

    Optional<Event> findByIdAndInitiatorId(int id, int initiatorId);
}
