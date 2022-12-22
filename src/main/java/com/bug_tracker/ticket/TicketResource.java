package com.bug_tracker.ticket;

import com.bug_tracker.app_user.AppUser;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
public class TicketResource {

    private final TicketService ticketService;

    public TicketResource(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets(HttpServletRequest request) {
        //var appUser = (AppUser) request.getAttribute("appUser");

        return ResponseEntity.ok(ticketService.findAll());
    }

    @GetMapping("/fromToken")
    public ResponseEntity<List<TicketDTO>> getAppUserByToken(HttpServletRequest request) {
        
        var appUser = (AppUser) request.getAttribute("appUser");
        
        return ResponseEntity.ok(ticketService.findAllByAppUserId(appUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable final Long id) {
        return ResponseEntity.ok(ticketService.get2(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTicket(@RequestBody @Valid final TicketDTO ticketDTO) {
        return new ResponseEntity<>(ticketService.create(ticketDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTicket(@PathVariable final Long id,
                                             @RequestBody @Valid final TicketDTO ticketDTO) {
        ticketService.update(id, ticketDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTicket(@PathVariable final Long id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
