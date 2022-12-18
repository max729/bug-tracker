package com.bug_tracker.comment;

import com.bug_tracker.app_user.AppUser;
import com.bug_tracker.app_user.AppUserRepository;
import com.bug_tracker.ticket.Ticket;
import com.bug_tracker.ticket.TicketRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AppUserRepository appUserRepository;
    private final TicketRepository ticketRepository;

    public CommentService(final CommentRepository commentRepository,
            final AppUserRepository appUserRepository, final TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.appUserRepository = appUserRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<CommentDTO> findAll() {
        return commentRepository.findAll(Sort.by("id"))
                .stream()
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .collect(Collectors.toList());
    }

    public CommentDTO get(final Long id) {
        return commentRepository.findById(id)
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final CommentDTO commentDTO) {
        final Comment comment = new Comment();
        mapToEntity(commentDTO, comment);
        return commentRepository.save(comment).getId();
    }

    public void update(final Long id, final CommentDTO commentDTO) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(commentDTO, comment);
        commentRepository.save(comment);
    }

    public void delete(final Long id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO mapToDTO(final Comment comment, final CommentDTO commentDTO) {
        commentDTO.setId(comment.getId());
        commentDTO.setComment(comment.getComment());
        commentDTO.setUserLink(comment.getUserLink() == null ? null : comment.getUserLink().getId());
        commentDTO.setTicket(comment.getTicket() == null ? null : comment.getTicket().getId());
        return commentDTO;
    }

    private Comment mapToEntity(final CommentDTO commentDTO, final Comment comment) {
        comment.setComment(commentDTO.getComment());
        final AppUser userLink = commentDTO.getUserLink() == null ? null : appUserRepository.findById(commentDTO.getUserLink())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "userLink not found"));
        comment.setUserLink(userLink);
        final Ticket ticket = commentDTO.getTicket() == null ? null : ticketRepository.findById(commentDTO.getTicket())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ticket not found"));
        comment.setTicket(ticket);
        return comment;
    }

}
