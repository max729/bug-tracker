package com.bug_tracker.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByProjectNameIgnoreCase(String projectName);

//SELECT DISTINCT p FROM Project p JOIN FETCH p.allUserAppUsers u WHERE u.id = :appUserId

    @Query("""
        SELECT u.allUserProjects FROM AppUser u Where u.id = :appUserId
        """)
    List<Project> findByAppUserId(String appUserId);

}
