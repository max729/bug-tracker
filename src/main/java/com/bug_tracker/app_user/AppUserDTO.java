package com.bug_tracker.app_user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppUserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    private String email;

    private UserRole userRole;

}
