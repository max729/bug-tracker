package com.bug_tracker.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByProjectNameIgnoreCase(String projectName);



    @Query("""
        SELECT p FROM Project p JOIN FETCH p.allUserAppUsers u WHERE :appUserId = u.id
        """)
    List<Project> findByAppUserId(String appUserId);

}
