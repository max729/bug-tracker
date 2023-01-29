package com.bug_tracker.app_user;

import com.bug_tracker.app_user.dtos.AppUserDTO;
import com.bug_tracker.app_user.dtos.AppUserStatsDTO;
import com.bug_tracker.auth.Jwt;
import com.bug_tracker.auth.Login;
import com.bug_tracker.email.MailServices;
import com.bug_tracker.ticket.Priority;
import com.bug_tracker.ticket.Ticket;
import com.bug_tracker.ticket.TicketStatus;
import com.bug_tracker.ticket.TicketRepository;

import java.util.EnumMap;
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
    private final TicketRepository ticketRepository;


    private final PasswordEncoder passwordEncoder;
    private final String accessTokenSecret;
    private final String refreshTokenSecret;
    private final String forgotTokenSecret;

    private final MailServices mailServices;

    public AppUserService(final AppUserRepository appUserRepository,
                        TicketRepository ticketsRepository,
                          final PasswordEncoder passwordEncoder,
                          @Value("${application.security.access-token-secret}") String accessTokenSecret,
                          @Value("${application.security.refresh-token-secret}") String refreshTokenSecret,
                          @Value("${application.security.forgot-token-secret}")String forgotTokenSecret,
                          MailServices mailServices) {
        this.appUserRepository = appUserRepository;
        this.ticketRepository = ticketsRepository;
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

    public AppUserDTO get(final String id) {
        return appUserRepository.findById(id)
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public String create(final AppUserDTO appUserDTO) {
        
        if(  appUserDTO.getId().contains(" ")  ){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Username: No space allowed");
        }

        if( appUserDTO.getPassword().equals("") ||  appUserDTO.getPassword().contains(" ")  ) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"password empty");
        }

        final AppUser appUser = new AppUser();
        mapToEntity(appUserDTO, appUser);

        appUser.setUserRole(UserRole.GUEST);

        return appUserRepository.save(appUser).getId();
    }

    public void update(final String id, final AppUserDTO appUserDTO) {
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(appUserDTO, appUser);
        appUserRepository.save(appUser);
    }

    public void delete(final String id) {
        appUserRepository.deleteById(id);
    }

    public AppUserDTO mapToDTO(final AppUser appUser, final AppUserDTO appUserDTO) {
        appUserDTO.setId(appUser.getId());
        appUserDTO.setFirstName(appUser.getFirstName());
        appUserDTO.setLastName(appUser.getLastName());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setUserRole(appUser.getUserRole());
        return appUserDTO;
    }

    private AppUser mapToEntity(final AppUserDTO appUserDTO, final AppUser appUser) {
        appUser.setId(appUserDTO.getId());
        appUser.setFirstName(appUserDTO.getFirstName());
        appUser.setLastName(appUserDTO.getLastName());
        appUser.setEmail(appUserDTO.getEmail());
        appUser.setUserRole(appUserDTO.getUserRole());
        if( appUserDTO.getPassword() != null )
            appUser.setPassword( passwordEncoder.encode(appUserDTO.getPassword()));
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

        var appUser =  getUserFromToken(token);

        appUser.setPassword( passwordEncoder.encode(newPassword));

        appUserRepository.save(appUser);

    }

    public AppUserStatsDTO AppUserStats(String id) {

        EnumMap<Priority, Integer> totalTicktetsByPriority = new EnumMap<Priority, Integer>(Priority.class);
        EnumMap<TicketStatus, Integer> totalTicktetsByStatus = new EnumMap<TicketStatus, Integer>(TicketStatus.class);
        
        for (Priority ele : Priority.values()) { 
            totalTicktetsByPriority.put(ele,0);
        }
        for (TicketStatus ele : TicketStatus.values()) { 
            totalTicktetsByStatus.put(ele,0);
        }

        List<Ticket> tickets =  ticketRepository.findAll();//findTicketsByUserProjekts(id);

        int totalTickets = 0;

        for (Ticket ticket: tickets) {
            totalTickets++;
            totalTicktetsByStatus.put(ticket.getStatus(), totalTicktetsByStatus.get(ticket.getStatus())+1);
            totalTicktetsByPriority.put(ticket.getPriority(), totalTicktetsByPriority.get(ticket.getPriority())+1);
        }

        AppUserStatsDTO stats = new AppUserStatsDTO();

        stats.setTotalTicktets(totalTickets);
        stats.setTotalTicktetsByPriority(totalTicktetsByPriority);
        stats.setTotalTicktetsByStatus(totalTicktetsByStatus);

        return stats;
    }
}
