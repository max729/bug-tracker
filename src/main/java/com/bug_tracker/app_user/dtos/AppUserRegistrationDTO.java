package com.bug_tracker.app_user.dtos;

import com.bug_tracker.app_user.UserRole;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppUserRegistrationDTO extends AppUserDTO {
    @NotNull
    @Size(max = 24)
    private String id;


    @Size(max = 255)
    private String firstName;


    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    private String email;


    @Size(max = 255)
    private String password;

    private UserRole userRole;
    
}
