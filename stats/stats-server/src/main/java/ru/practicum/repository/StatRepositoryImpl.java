package ru.practicum.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.model.QEndpointHit;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StatRepositoryImpl implements StatRepositoryCustom {
    private final ViewStatsMapper viewStatsMapper;
    private final EntityManager entityManager;

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean isUnique) {
        QEndpointHit endpointHit = QEndpointHit.endpointHit;

        BooleanExpression where = endpointHit.timestamp.between(start, end);

        if (uris != null && !uris.isEmpty()) {
            where = where.and(endpointHit.uri.in(uris));
        }

        return new JPAQuery<Tuple>(entityManager)
                .select(endpointHit.app, endpointHit.uri, isUnique ? endpointHit.ip.countDistinct() : Expressions.ONE.count())
                .from(endpointHit)
                .where(where)
                .groupBy(endpointHit.app, endpointHit.uri)
                .orderBy(Expressions.THREE.desc())
                .stream()
                .map(viewStatsMapper::tupleToViewStatsDto)
                .collect(Collectors.toUnmodifiableList());
    }
}
