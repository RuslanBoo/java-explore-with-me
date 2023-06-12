package ru.practicum.events.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import ru.practicum.categories.model.Category;
import ru.practicum.events.enums.EventState;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User initiator;

    @Column(length = 7000)
    private String description;

    @Column(length = 120)
    private String title;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    private Double latitude;
    private Double longitude;
    private Boolean paid;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Formula(value = "(SELECT COUNT(r.id) FROM requests r " +
            "WHERE r.event_id = id " +
            "AND r.status = 'CONFIRMED' " +
            "GROUP BY r.id)")
    private Integer confirmedRequests;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;
}
