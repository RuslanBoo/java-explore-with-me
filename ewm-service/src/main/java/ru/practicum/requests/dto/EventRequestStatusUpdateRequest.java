package ru.practicum.requests.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.requests.enums.UpdateRequestStatus;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private UpdateRequestStatus status;
}
