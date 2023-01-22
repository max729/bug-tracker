package com.bug_tracker.project;

import com.bug_tracker.app_user.AppUser;
import com.bug_tracker.app_user.AppUserRepository;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Transactional
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AppUserRepository appUserRepository;

    public ProjectService(final ProjectRepository projectRepository,
                          final AppUserRepository appUserRepository) {
        this.projectRepository = projectRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<ProjectDTO> findAll() {
        return projectRepository.findAll(Sort.by("id"))
                .stream()
                .map(projekt -> mapToDTO(projekt, new ProjectDTO()))
                .collect(Collectors.toList());
    }

    public ProjectDTO get(final Long id) {
        return projectRepository.findById(id)
                .map(projekt -> mapToDTO(projekt, new ProjectDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<ProjectDTO> getByAppUserId(final String id) {
        return projectRepository.findByAppUserId(id)
                .stream()
                .map(projekt -> mapToDTO(projekt, new ProjectDTO()))
                .collect(Collectors.toList());                
    }

    public Long create(final ProjectDTO projectDTO) {
        final Project project = new Project();
        mapToEntity(projectDTO, project);
        return projectRepository.save(project).getId();
    }

    public void update(final Long id, final ProjectDTO projectDTO) {
        final Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(projectDTO, project);
        projectRepository.save(project);
    }

    public void delete(final Long id) {
        projectRepository.deleteById(id);
    }

    private ProjectDTO mapToDTO(final Project project, final ProjectDTO projectDTO) {
        projectDTO.setId(project.getId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectDescription(project.getProjectDescription());
        projectDTO.setDateCreated(project.getDateCreated());
        projectDTO.setAllUsers(project.getAllUserAppUsers() == null ? null : project.getAllUserAppUsers().stream()
                .map(appUser -> appUser.getId())
                .collect(Collectors.toList()));
        return projectDTO;
    }

    private Project mapToEntity(final ProjectDTO projectDTO, final Project project) {
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectDescription(projectDTO.getProjectDescription());
        final List<AppUser> allUsers = appUserRepository.findAllById(
                projectDTO.getAllUsers() == null ? Collections.emptyList() : projectDTO.getAllUsers());
        if (allUsers.size() != (projectDTO.getAllUsers() == null ? 0 : projectDTO.getAllUsers().size())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "one of allUsers not found");
        }
        project.setAllUserAppUsers(allUsers.stream().collect(Collectors.toSet()));
        return project;
    }

    public boolean projektNameExists(final String projektName) {
        return projectRepository.existsByProjectNameIgnoreCase(projektName);
    }


}
