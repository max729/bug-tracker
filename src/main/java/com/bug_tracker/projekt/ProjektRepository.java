package com.bug_tracker.projekt;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjektRepository extends JpaRepository<Projekt, Long> {

    boolean existsByProjektNameIgnoreCase(String projektName);

}
