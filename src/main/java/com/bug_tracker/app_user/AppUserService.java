package com.bug_tracker.app_user;

import com.bug_tracker.auth.Jwt;
import com.bug_tracker.auth.Login;
import com.bug_tracker.email.MailServices;
import com.bug_tracker.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
    private final String forgotTokenSecret;

    private final MailServices mailServices;

    public AppUserService(final AppUserRepository appUserRepository,
                          final PasswordEncoder passwordEncoder,
                          @Value("${application.security.access-token-secret}") String accessTokenSecret,
                          @Value("${application.security.refresh-token-secret}") String refreshTokenSecret,
                          @Value("${application.security.forgot-token-secret}")String forgotTokenSecret,
                          MailServices mailServices) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.forgotTokenSecret = forgotTokenSecret;
        this.mailServices = mailServices;
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


    public Login login(final String email, final String password) {

        var appUser = appUserRepository.findByEmail(email)
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"invalid credentials"));

        if(!passwordEncoder.matches(password,appUser.getPassword()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid credentials");


        return Login.of(
                appUser.getId(),
                accessTokenSecret,
                refreshTokenSecret
        );
    }

    //Logger logger = LoggerFactory.getLogger(AppUserService.class);
    public AppUser getUserFromToken(final String token) {

        var appUser = appUserRepository.findById(Jwt.from(token,accessTokenSecret))
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"cant find User by token"));

        return appUser;
    }

    public Login refreshAccess(final String refreshToken) {

        var appUserId = Jwt.from(refreshToken,refreshTokenSecret);

        return Login.of(
                appUserId,
                accessTokenSecret,
                Jwt.of(refreshToken)
        );
    }

    public void forget(String email, String originUrl) {
        var appUser = appUserRepository.findByEmail(email)
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"cant find User by Mail") );

        var token = Jwt.of(appUser.getId(),30L,forgotTokenSecret);

        mailServices.sendForgotMassage(email,token.getToken(),originUrl);
        //appUserRepository.save(appUser);

    }

    public void reset(String token, String newPassword) {

        var appUser = getUserFromToken(token);

        appUser.setPassword(newPassword);

        appUserRepository.save(appUser);

    }
}
