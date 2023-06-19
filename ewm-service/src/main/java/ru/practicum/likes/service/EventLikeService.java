package ru.practicum.likes.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.interval.IntervalUtils;
import org.springframework.stereotype.Service;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.DataNotFoundException;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.likes.enums.EventLikeType;
import ru.practicum.likes.model.EventLike;
import ru.practicum.likes.model.EventLikesCnt;
import ru.practicum.likes.repository.EventLikeRepository;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventLikeService {
    private final EventLikeRepository eventLikeRepository;
    private final EventRepository eventRepository;
    private final UserService userService;
    private static final float Z_SCORE = 0.96F;

    /**
     * Method for recount event rating from lower bound
     * of Wilson score confidence interval for a Bernoulli parameter
     **/
    private void recountEventRating(int eventId) {
        EventLikesCnt eventLikesCnt = eventLikeRepository.getEventLikes(eventId);
        int totalCnt = eventLikesCnt.getTotalCount();
        int likesCnt = eventLikesCnt.getLikesCount();
        float rating = 0F;

        if (likesCnt > 0 && totalCnt > 0) {
            rating = (float) IntervalUtils
                    .getWilsonScoreInterval(totalCnt, likesCnt, Z_SCORE)
                    .getLowerBound();
        }

        Event event = findEventById(eventId);
        event.setRating(rating);
        eventRepository.save(event);
    }

    private Event findEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException(Event.class.getName(), eventId)
                );
    }

    private EventLike findLikeByUser(int eventId, int userId) {
        return eventLikeRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new DataNotFoundException(EventLike.class.getName(), eventId)
                );
    }

    @Transactional
    public void addLike(int eventId, int userId, EventLikeType type) {
        userService.checkUserById(userId);
        Event event = findEventById(eventId);

        if (event.getInitiator().getId() == userId) {
            throw new ConflictException(String.format("User can not post %s for his event", type));
        }

        Optional<EventLike> eventLike = eventLikeRepository.findByEventIdAndUserId(eventId, userId);

        eventLike.ifPresent(
                like -> {
                    if (like.getType().equals(type)) {
                        throw new ConflictException(String.format("User can not post event %s twice", type));
                    } else {
                        eventLikeRepository.delete(like);
                    }
                }
        );

        EventLike like = EventLike.builder()
                .userId(userId)
                .eventId(eventId)
                .createdOn(LocalDateTime.now())
                .type(type)
                .build();

        eventLikeRepository.save(like);
        recountEventRating(eventId);
    }

    @Transactional
    public void removeLike(int eventId, int userId) {
        userService.checkUserById(userId);
        findEventById(eventId);

        EventLike like = findLikeByUser(eventId, userId);

        eventLikeRepository.delete(like);
        recountEventRating(eventId);
    }

    public List<UserDto> getEventLikeUsers(int eventId, EventLikeType type, int from, int size) {
        List<Integer> userIds = eventLikeRepository.findByEventIdAndType(eventId, type)
                .stream()
                .map(EventLike::getId)
                .collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return new ArrayList<>();
        }

        return userService.findAllByUser(userIds, from, size);
    }
}
