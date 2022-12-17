package com.bug_tracker.projekt;

import com.bug_tracker.app_user.AppUser;
import com.bug_tracker.app_user.AppUserRepository;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Transactional
@Service
public class ProjektService {

    private final ProjektRepository projektRepository;
    private final AppUserRepository appUserRepository;

    public ProjektService(final ProjektRepository projektRepository,
            final AppUserRepository appUserRepository) {
        this.projektRepository = projektRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<ProjektDTO> findAll() {
        return projektRepository.findAll(Sort.by("id"))
                .stream()
                .map(projekt -> mapToDTO(projekt, new ProjektDTO()))
                .collect(Collectors.toList());
    }

    public ProjektDTO get(final Long id) {
        return projektRepository.findById(id)
                .map(projekt -> mapToDTO(projekt, new ProjektDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final ProjektDTO projektDTO) {
        final Projekt projekt = new Projekt();
        mapToEntity(projektDTO, projekt);
        return projektRepository.save(projekt).getId();
    }

    public void update(final Long id, final ProjektDTO projektDTO) {
        final Projekt projekt = projektRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(projektDTO, projekt);
        projektRepository.save(projekt);
    }

    public void delete(final Long id) {
        projektRepository.deleteById(id);
    }

    private ProjektDTO mapToDTO(final Projekt projekt, final ProjektDTO projektDTO) {
        projektDTO.setId(projekt.getId());
        projektDTO.setProjektName(projekt.getProjektName());
        projektDTO.setProjektDescription(projekt.getProjektDescription());
        projektDTO.setDateCreated(projekt.getDateCreated());
        projektDTO.setAllUsers(projekt.getAllUserAppUsers() == null ? null : projekt.getAllUserAppUsers().stream()
                .map(appUser -> appUser.getId())
                .collect(Collectors.toList()));
        return projektDTO;
    }

    private Projekt mapToEntity(final ProjektDTO projektDTO, final Projekt projekt) {
        projekt.setProjektName(projektDTO.getProjektName());
        projekt.setProjektDescription(projektDTO.getProjektDescription());
        final List<AppUser> allUsers = appUserRepository.findAllById(
                projektDTO.getAllUsers() == null ? Collections.emptyList() : projektDTO.getAllUsers());
        if (allUsers.size() != (projektDTO.getAllUsers() == null ? 0 : projektDTO.getAllUsers().size())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "one of allUsers not found");
        }
        projekt.setAllUserAppUsers(allUsers.stream().collect(Collectors.toSet()));
        return projekt;
    }

    public boolean projektNameExists(final String projektName) {
        return projektRepository.existsByProjektNameIgnoreCase(projektName);
    }


}
