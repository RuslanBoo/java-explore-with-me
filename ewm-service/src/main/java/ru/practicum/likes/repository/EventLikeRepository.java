package ru.practicum.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.likes.enums.EventLikeType;
import ru.practicum.likes.model.EventLike;
import ru.practicum.likes.model.EventLikesCnt;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Integer> {
    List<EventLike> findByEventId(int eventId);

    List<EventLike> findByEventIdAndType(int eventId, EventLikeType type);

    Optional<EventLike> findByEventIdAndUserId(int eventId, int userId);

    @Query(value = "SELECT COUNT(e.id) totalCount, COUNT(CASE WHEN e.like_type = 'LIKE' THEN 1 END) likesCount " +
            "FROM event_likes e " +
            "WHERE e.event_id = :id", nativeQuery = true)
    EventLikesCnt getEventLikes(@Param("id") int id);
}
