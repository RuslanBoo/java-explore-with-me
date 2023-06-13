package ru.practicum.events.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ViewStatsDto;
import ru.practicum.client.StatClient;
import ru.practicum.core.pagination.PaginationService;
import ru.practicum.error.exception.BadRequestException;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.DataNotFoundException;
import ru.practicum.events.EventMapper;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.enums.AdminStateAction;
import ru.practicum.events.enums.EventSort;
import ru.practicum.events.enums.EventState;
import ru.practicum.events.enums.UserStateAction;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.requests.enums.UpdateRequestStatus;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final RequestRepository requestRepository;
    private final PaginationService paginationService;
    private final StatClient statClient;
    private final ObjectMapper objectMapper;

    private long getStat(Event event) {
        long views = 0;

        List<String> uri = List.of("/events/" + event.getId());
        Object stats = statClient.getStats(LocalDateTime.of(1970, 1, 1, 0, 0, 0), LocalDateTime.now(), uri).getBody();

        if (stats != null) {
            List<ViewStatsDto> statResult = objectMapper.convertValue(stats, new TypeReference<>() {
            });

            for (ViewStatsDto stat : statResult) {
                String statAppName = "ewm-main-service";
                if (stat.getUri().equals("/events/" + event.getId()) && stat.getApp().equals(statAppName)) {
                    views = stat.getHits();
                    break;
                }
            }
        }

        return views;
    }

    public EventFullDto prepareFullDto(Event event) {
        EventFullDto eventfullDto = eventMapper.toFullDto(event);
        eventfullDto.setViews(getStat(event));

        return eventfullDto;
    }

    public Event findById(int eventId) {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            throw new DataNotFoundException(Event.class.getName(), eventId);
        }

        return event.get();
    }

    public Set<Event> findAllById(Set<Integer> ids) {
        return eventRepository.findAllByIdIn(ids);
    }

    public List<EventShortDto> getByUserId(int userId, int from, int size) {
        userService.checkUserById(userId);
        Pageable pageable = paginationService.getPageable(from, size);
        List<Event> result = eventRepository.findAllByInitiatorId(userId, pageable);

        return result.stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getByEventId(int userId, int eventId) {
        userService.checkUserById(userId);
        Optional<Event> event = eventRepository.findByIdAndInitiatorId(eventId, userId);

        if (event.isEmpty()) {
            throw new DataNotFoundException(Event.class.getName(), eventId);
        }

        return prepareFullDto(event.get());
    }

    public EventFullDto save(int userId, NewEventDto newEventDto) {
        User initiator = userService.checkUserById(userId);
        Event event = eventMapper.fromNewDto(newEventDto);

        event.setState(EventState.PENDING);
        event.setInitiator(initiator);
        event.setCreatedOn(LocalDateTime.now());

        if (newEventDto.getPaid() == null) {
            event.setPaid(false);
        }

        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }

        return prepareFullDto(eventRepository.save(event));
    }

    public EventFullDto update(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
        userService.checkUserById(userId);
        Event event = findById(eventId);

        if (event.getInitiator().getId() != userId) {
            throw new DataNotFoundException(Event.class.getName(), eventId);
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Event for update must not be published");
        }

        if (updateEventUserRequest.getStateAction() == UserStateAction.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        } else if (updateEventUserRequest.getStateAction() == UserStateAction.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }

        eventMapper.update(updateEventUserRequest, event);
        eventRepository.save(event);

        return prepareFullDto(findById(eventId));
    }

    public List<EventFullDto> searchByAdmin(
            List<Integer> users, List<EventState> states,
            List<Integer> categories, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, int from, int size) {

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("date from must be earlier then date to");
        }

        List<Event> result = eventRepository.findAllByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);

        return result.stream()
                .map(this::prepareFullDto)
                .collect(Collectors.toList());
    }

    public EventFullDto updateByAdmin(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = findById(eventId);

        if (LocalDateTime.now().isAfter(event.getEventDate().minusHours(1))) {
            throw new ConflictException("Event date must be more 1 hour after publish");
        }

        eventMapper.update(updateEventAdminRequest, event);

        if (updateEventAdminRequest.getStateAction() == AdminStateAction.PUBLISH_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new ConflictException("Event must be \"pending\" status for publish");
            }

            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (updateEventAdminRequest.getStateAction() == AdminStateAction.REJECT_EVENT) {
            if (event.getState() == EventState.PUBLISHED) {
                throw new ConflictException("Event must not be \"published\" status for cancel");
            }

            event.setState(EventState.CANCELED);
        }

        eventRepository.save(event);

        return prepareFullDto(findById(eventId));
    }

    public List<EventShortDto> searchByUser(String text, Boolean paid, Boolean onlyAvailable, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, EventSort sort, int from, int size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("date from must be earlier then date to");
        }

        List<Event> result = eventRepository.findAllByUser(text, onlyAvailable, paid, categories, rangeStart, rangeEnd, sort, from, size);

        return result.stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto publicFindById(int eventId) {
        Event event = findById(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new DataNotFoundException(Event.class.getName(), eventId);
        }

        event.setConfirmedRequests(requestRepository.countAllByEventIdAndStatus(eventId, UpdateRequestStatus.CONFIRMED));

        return prepareFullDto(event);
    }
}
