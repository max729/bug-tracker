package com.bug_tracker.tickets;

import com.bug_tracker.app_user.AppUser;
import com.bug_tracker.app_user.AppUserRepository;
import com.bug_tracker.projekt.Projekt;
import com.bug_tracker.projekt.ProjektRepository;
import com.bug_tracker.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class TicketsService {

    private final TicketsRepository ticketsRepository;
    private final ProjektRepository projektRepository;
    private final AppUserRepository appUserRepository;

    public TicketsService(final TicketsRepository ticketsRepository,
            final ProjektRepository projektRepository, final AppUserRepository appUserRepository) {
        this.ticketsRepository = ticketsRepository;
        this.projektRepository = projektRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<TicketsDTO> findAll() {
        return ticketsRepository.findAll(Sort.by("id"))
                .stream()
                .map(tickets -> mapToDTO(tickets, new TicketsDTO()))
                .collect(Collectors.toList());
    }

    public TicketsDTO get(final Long id) {
        return ticketsRepository.findById(id)
                .map(tickets -> mapToDTO(tickets, new TicketsDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final TicketsDTO ticketsDTO) {
        final Tickets tickets = new Tickets();
        mapToEntity(ticketsDTO, tickets);
        return ticketsRepository.save(tickets).getId();
    }

    public void update(final Long id, final TicketsDTO ticketsDTO) {
        final Tickets tickets = ticketsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(ticketsDTO, tickets);
        ticketsRepository.save(tickets);
    }

    public void delete(final Long id) {
        ticketsRepository.deleteById(id);
    }

    private TicketsDTO mapToDTO(final Tickets tickets, final TicketsDTO ticketsDTO) {
        ticketsDTO.setId(tickets.getId());
        ticketsDTO.setName(tickets.getName());
        ticketsDTO.setStatus(tickets.getStatus());
        ticketsDTO.setDescription(tickets.getDescription());
        ticketsDTO.setPriority(tickets.getPriority());
        ticketsDTO.setTyp(tickets.getTyp());
        ticketsDTO.setProjektLink(tickets.getProjektLink() == null ? null : tickets.getProjektLink().getId());
        ticketsDTO.setAuthor(tickets.getAuthor() == null ? null : tickets.getAuthor().getId());
        ticketsDTO.setAssigned(tickets.getAssigned() == null ? null : tickets.getAssigned().getId());
        return ticketsDTO;
    }

    private Tickets mapToEntity(final TicketsDTO ticketsDTO, final Tickets tickets) {
        tickets.setName(ticketsDTO.getName());
        tickets.setStatus(ticketsDTO.getStatus());
        tickets.setDescription(ticketsDTO.getDescription());
        tickets.setPriority(ticketsDTO.getPriority());
        tickets.setTyp(ticketsDTO.getTyp());
        final Projekt projektLink = ticketsDTO.getProjektLink() == null ? null : projektRepository.findById(ticketsDTO.getProjektLink())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "projektLink not found"));
        tickets.setProjektLink(projektLink);
        final AppUser author = ticketsDTO.getAuthor() == null ? null : appUserRepository.findById(ticketsDTO.getAuthor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "author not found"));
        tickets.setAuthor(author);
        final AppUser assigned = ticketsDTO.getAssigned() == null ? null : appUserRepository.findById(ticketsDTO.getAssigned())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "assigned not found"));
        tickets.setAssigned(assigned);
        return tickets;
    }

    @Transactional
    public String getReferencedWarning(final Long id) {
        final Tickets tickets = ticketsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!tickets.getTicketComments().isEmpty()) {
            return WebUtils.getMessage("tickets.comment.oneToMany.referenced", tickets.getTicketComments().iterator().next().getId());
        }
        return null;
    }

}
