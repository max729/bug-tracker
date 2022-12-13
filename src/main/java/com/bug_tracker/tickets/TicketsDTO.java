package com.bug_tracker.tickets;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TicketsDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private TicketStatus status;

    @NotNull
    @Size(max = 255)
    private String description;

    @NotNull
    private Priority priority;

    @NotNull
    private TicketType typ;

    private Long projektLink;

    @NotNull
    private Long author;

    private Long assigned;

    private OffsetDateTime lastUpdated;

}
