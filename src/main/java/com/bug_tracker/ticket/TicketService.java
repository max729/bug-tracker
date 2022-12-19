package com.bug_tracker.ticket;

import com.bug_tracker.app_user.AppUser;
import com.bug_tracker.app_user.AppUserRepository;
import com.bug_tracker.comment.CommentRepository;
import com.bug_tracker.comment.CommentService;
import com.bug_tracker.project.Project;
import com.bug_tracker.project.ProjectRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final AppUserRepository appUserRepository;
    private final CommentService commentService;

    public TicketService(final TicketRepository ticketRepository,
                         final ProjectRepository projectRepository,
                         final AppUserRepository appUserRepository,
                         final CommentService commentService) {
        this.ticketRepository = ticketRepository;
        this.projectRepository = projectRepository;
        this.appUserRepository = appUserRepository;
        this.commentService = commentService;
    }

    public List<TicketDTO> findAll() {
        //
        //.findTicketsByUserProjekts(id)
        return ticketRepository.findAll(Sort.by("id"))
                .stream()
                .map(tickets -> mapToDTO(tickets, new TicketDTO()))
                .collect(Collectors.toList());
    }

    public TicketDTO get(final Long id) {

        TicketDTO ticketDTO =  ticketRepository.findById(id)
                .map(tickets -> mapToDTO(tickets, new TicketDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        commentService.findAllByTicketId(id)  ;
                //ticketDTO.setTicketComments(  );

        return ticketDTO;
    }

    public Long create(final TicketDTO ticketDTO) {
        final Ticket ticket = new Ticket();
        mapToEntity(ticketDTO, ticket);
        return ticketRepository.save(ticket).getId();
    }

    public void update(final Long id, final TicketDTO ticketDTO) {
        final Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(ticketDTO, ticket);
        ticketRepository.save(ticket);
    }

    public void delete(final Long id) {
        ticketRepository.deleteById(id);
    }

    private TicketDTO mapToDTO(final Ticket ticket, final TicketDTO ticketDTO) {
        ticketDTO.setId(ticket.getId());
        ticketDTO.setName(ticket.getName());
        ticketDTO.setStatus(ticket.getStatus());
        ticketDTO.setDescription(ticket.getDescription());
        ticketDTO.setPriority(ticket.getPriority());
        ticketDTO.setTyp(ticket.getTyp());
        ticketDTO.setProjektLink(ticket.getProjectLink() == null ? null : ticket.getProjectLink().getId());
        

        ticketDTO.setAuthor(ticket.getAuthor() == null ? null : ticket.getAuthor().getId());
        /*appUserRepository.findById( 
            tickets.getAuthor().getId() )
        .orElse(null).getFirstName()  
        );*/
        ticketDTO.setAssigned(ticket.getAssigned() == null ? null : ticket.getAssigned().getId());
        ticketDTO.setLastUpdated(ticket.getLastUpdated());

        return ticketDTO;
    }

    private Ticket mapToEntity(final TicketDTO ticketDTO, final Ticket ticket) {
        ticket.setName(ticketDTO.getName());
        ticket.setStatus(ticketDTO.getStatus());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setTyp(ticketDTO.getTyp());
        final Project projektLink = ticketDTO.getProjektLink() == null ? null : projectRepository.findById(ticketDTO.getProjektLink())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "projektLink not found"));
        ticket.setProjectLink(projektLink);
        final AppUser author = ticketDTO.getAuthor() == null ? null : appUserRepository.findById(ticketDTO.getAuthor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "author not found"));
        ticket.setAuthor(author);
        final AppUser assigned = ticketDTO.getAssigned() == null ? null : appUserRepository.findById(ticketDTO.getAssigned())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "assigned not found"));
        ticket.setAssigned(assigned);
        return ticket;
    }


}
