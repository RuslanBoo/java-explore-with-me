package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.EndpointHit;

public interface StatRepository extends JpaRepository<EndpointHit, Integer>, QuerydslPredicateExecutor<EndpointHit>, StatRepositoryCustom {
}

