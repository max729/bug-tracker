package com.bug_tracker.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String comment;

    @NotNull
    private Long userLink;

    private Long ticket;

}
