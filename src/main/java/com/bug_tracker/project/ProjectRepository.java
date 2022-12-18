package com.bug_tracker.project;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByProjectNameIgnoreCase(String projectName);

}
