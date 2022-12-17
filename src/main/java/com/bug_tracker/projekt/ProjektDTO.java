package com.bug_tracker.projekt;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProjektDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String projektName;

    @NotNull
    @Size(max = 255)
    private String projektDescription;

    private OffsetDateTime dateCreated;

    private List<Long> allUsers;

    private List<String> allUsersNames;

}
