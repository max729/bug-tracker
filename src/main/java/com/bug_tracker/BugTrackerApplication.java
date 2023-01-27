package com.bug_tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.bug_tracker.app_user.dtos.AppUserDTO;
import com.bug_tracker.app_user.AppUserService;
import com.bug_tracker.app_user.UserRole;


@SpringBootApplication
public class BugTrackerApplication implements CommandLineRunner {

    public static void main(final String[] args) {
        SpringApplication.run(BugTrackerApplication.class, args);
    }

    @Autowired
    private AppUserService appUserService;

    @Override
    public void run(String... args) {

        var app_user = new AppUserDTO();

        app_user.setPassword("12345");
        app_user.setEmail("max@mail.com");
        app_user.setId("maxS");
        app_user.setUserRole(UserRole.ADMIN);
        app_user.setLastName("S");
        app_user.setFirstName("max");

        appUserService.create(app_user);
        appUserService.update(app_user.getId(), app_user);   
        
        
        app_user.setPassword("12345");
        app_user.setEmail("guest@mail.com");
        app_user.setId("Guest");
        app_user.setLastName("Guest");
        app_user.setFirstName("Guest");
        appUserService.create(app_user);

    }



}