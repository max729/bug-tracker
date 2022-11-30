package com.bug_tracker.app_user;

import com.bug_tracker.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    private final String accessTokenSecret;
    private final String refreshTokenSecret;

    public AppUserService(final AppUserRepository appUserRepository,
                          final PasswordEncoder passwordEncoder,
                          @Value("${application.security.access-token-secret") String accessTokenSecret,
                          @Value("${application.security.refresh-token-secret") String refreshTokenSecret
                          ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
    }

    public List<AppUserDTO> findAll() {
        return appUserRepository.findAll(Sort.by("id"))
                .stream()
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .collect(Collectors.toList());
    }

    public AppUserDTO get(final Long id) {
        return appUserRepository.findById(id)
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final AppUserDTO appUserDTO) {
        final AppUser appUser = new AppUser();
        mapToEntity(appUserDTO, appUser);
        return appUserRepository.save(appUser).getId();
    }

    public void update(final Long id, final AppUserDTO appUserDTO) {
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(appUserDTO, appUser);
        appUserRepository.save(appUser);
    }

    public void delete(final Long id) {
        appUserRepository.deleteById(id);
    }

    private AppUserDTO mapToDTO(final AppUser appUser, final AppUserDTO appUserDTO) {
        appUserDTO.setId(appUser.getId());
        appUserDTO.setFirstName(appUser.getFirstName());
        appUserDTO.setLastName(appUser.getLastName());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setUserRole(appUser.getUserRole());
        return appUserDTO;
    }

    private AppUser mapToEntity(final AppUserDTO appUserDTO, final AppUser appUser) {
        appUser.setFirstName(appUserDTO.getFirstName());
        appUser.setLastName(appUserDTO.getLastName());
        appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        appUser.setEmail(appUserDTO.getEmail());
        appUser.setUserRole(appUserDTO.getUserRole());
        return appUser;
    }

    public boolean emailExists(final String email) {
        return appUserRepository.existsByEmailIgnoreCase(email);
    }

    @Transactional
    public String getReferencedWarning(final Long id) {
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!appUser.getUserLinkComments().isEmpty()) {
            return WebUtils.getMessage("appUser.comment.oneToMany.referenced", appUser.getUserLinkComments().iterator().next().getId());
        } else if (!appUser.getAuthorTicketss().isEmpty()) {
            return WebUtils.getMessage("appUser.tickets.manyToOne.referenced", appUser.getAuthorTicketss().iterator().next().getId());
        } else if (!appUser.getAssignedTicketss().isEmpty()) {
            return WebUtils.getMessage("appUser.tickets.manyToOne.referenced", appUser.getAssignedTicketss().iterator().next().getId());
        } else if (!appUser.getAllUserProjekts().isEmpty()) {
            return WebUtils.getMessage("appUser.projekt.manyToMany.referenced", appUser.getAllUserProjekts().iterator().next().getId());
        }
        return null;
    }

    public Login login(final String email,final String password) {

        var appUser = appUserRepository.findByEmail(email)
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid credentials"));

        if(!passwordEncoder.matches(password,appUser.getPassword()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid password");


        return Login.of(
                appUser.getId(),
                accessTokenSecret,
                refreshTokenSecret
        );
    }

    //Logger logger = LoggerFactory.getLogger(AppUserService.class);
    public AppUserDTO getUserFromToken(final String token) {

        var appUser = appUserRepository.findById(Token.from(token,accessTokenSecret))
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"cant find User_token"));

        return mapToDTO(appUser,new AppUserDTO());
    }

    public Login refreshAccess(final String refreshToken) {

        var appUserId = Token.from(refreshToken,refreshTokenSecret);

        return Login.of(
                appUserId,
                accessTokenSecret,
                Token.of(refreshToken)
        );
    }
}
