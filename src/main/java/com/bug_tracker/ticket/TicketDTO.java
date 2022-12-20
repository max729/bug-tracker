package com.bug_tracker.ticket;

import java.time.OffsetDateTime;
import java.util.List;

import com.bug_tracker.comment.CommentDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TicketDTO {

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
    private String author;

    private String assigned;

    private OffsetDateTime lastUpdated;

    private List<CommentDTO>  ticketComments;

}
