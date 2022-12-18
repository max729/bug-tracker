package com.bug_tracker.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProjectDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String projectName;

    @NotNull
    @Size(max = 255)
    private String projectDescription;

    private OffsetDateTime dateCreated;

    private List<String> allUsers;

    //private List<String> allUsersNames;

}
