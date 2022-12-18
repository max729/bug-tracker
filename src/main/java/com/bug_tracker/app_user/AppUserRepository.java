package com.bug_tracker.app_user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUser, String> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<AppUser> findByEmail(String email);


}
