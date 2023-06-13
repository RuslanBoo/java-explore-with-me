package ru.practicum.compilation.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.QCompilation;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CompilationRepositoryImpl implements CompilationRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public List<Compilation> findAllByPinned(Boolean pinned, int from, int size) {
        QCompilation compilation = QCompilation.compilation;
        BooleanExpression where = Expressions.asBoolean(true).isTrue();

        if (pinned != null) {
            where = where.and(compilation.pinned.eq(pinned));
        }

        return new JPAQuery<Compilation>(entityManager)
                .from(compilation)
                .where(where)
                .offset(from)
                .limit(size)
                .stream()
                .collect(Collectors.toList());
    }
}
