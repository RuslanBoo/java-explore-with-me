package ru.practicum.requests.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.requests.enums.UpdateRequestStatus;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Setter
@Getter
@RequiredArgsConstructor
public class ParticipationRequestDto {
    private int id;

    @Positive(message = "Field: event. Error: must not be null. Value: ${validatedValue}")
    private int event;

    @Positive(message = "Field: requester. Error: must not be null. Value: ${validatedValue}")
    private int requester;
    private UpdateRequestStatus status;
    private LocalDateTime created;
}
