package ru.practicum.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.DataNotFoundException;
import ru.practicum.events.enums.EventState;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.EventService;
import ru.practicum.requests.RequestMapper;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.enums.UpdateRequestStatus;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;
    private final EventService eventService;

    private Request findById(int requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException(Request.class.getName(), requestId)
                );
    }


    private Integer getAvailableRequestsLimit(int eventId) {
        Event event = eventService.findById(eventId);
        Integer confirmedRequestsCnt = requestRepository.countAllByEventIdAndStatus(eventId, UpdateRequestStatus.CONFIRMED);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return 1;
        }

        return event.getParticipantLimit() - confirmedRequestsCnt;
    }

    public List<ParticipationRequestDto> getByEventId(int userId, int eventId) {
        userService.checkUserById(userId);

        if (eventService.findById(eventId).getInitiator().getId() != userId) {
            throw new DataNotFoundException(Event.class.getName(), eventId);
        }

        List<Request> result = requestRepository.getAllByEventId(eventId);

        return result.stream()
                .map(requestMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult updateAllByEvent(int userId, int eventId, EventRequestStatusUpdateRequest updateRequest) {
        userService.checkUserById(userId);
        eventService.findById(eventId);

        if (getAvailableRequestsLimit(eventId) == 0) {
            throw new ConflictException("For the requested operation the conditions are not met.");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (int requestId : updateRequest.getRequestIds()) {
            Request request = findById(requestId);

            if (request.getStatus() != UpdateRequestStatus.PENDING) {
                throw new ConflictException("Request must have status PENDING");
            }

            switch (updateRequest.getStatus()) {
                case CONFIRMED:
                    if (getAvailableRequestsLimit(eventId) > 0) {
                        request.setStatus(UpdateRequestStatus.CONFIRMED);
                        confirmedRequests.add(requestMapper.toShortDto(request));
                        break;
                    }
                case REJECTED:
                    request.setStatus(UpdateRequestStatus.REJECTED);
                    rejectedRequests.add(requestMapper.toShortDto(request));
                    break;
            }

            requestRepository.save(request);
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    public List<ParticipationRequestDto> getByUserId(int userId) {
        userService.checkUserById(userId);
        List<Request> result = requestRepository.getAllByRequesterId(userId);

        return result.stream()
                .map(requestMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto add(int userId, int eventId) {
        User user = userService.checkUserById(userId);
        Event event = eventService.findById(eventId);

        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("User can not send request for self event");
        } else if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Event for participation request must be published");
        } else if (!requestRepository.getAllByEventIdAndRequesterId(eventId, userId).isEmpty()) {
            throw new ConflictException("Request from user already exist");
        } else if (event.getParticipantLimit() > 0
                && event.getConfirmedRequests() != null
                && event.getParticipantLimit() - event.getConfirmedRequests() == 0) {
            throw new ConflictException("Event request limit has been reached");
        }

        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequester(user);
        request.setEvent(event);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(UpdateRequestStatus.CONFIRMED);
        } else {
            request.setStatus(UpdateRequestStatus.PENDING);
        }

        return requestMapper.toShortDto(requestRepository.save(request));
    }

    public ParticipationRequestDto cancel(int userId, int requestId) {
        userService.checkUserById(userId);
        Request request = findById(requestId);

        if (request.getRequester().getId() != userId) {
            throw new DataNotFoundException(Request.class.getName(), requestId);
        }

        request.setStatus(UpdateRequestStatus.CANCELED);

        return requestMapper.toShortDto(requestRepository.save(request));
    }
}
