package com.bug_tracker.tickets;

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
public class TicketsResource {

    private final TicketsService ticketsService;

    public TicketsResource(final TicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    @GetMapping
    public ResponseEntity<List<TicketsDTO>> getAllTicketss(HttpServletRequest request) {
        var appUser = (AppUser) request.getAttribute("appUser");

        return ResponseEntity.ok(ticketsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketsDTO> getTickets(@PathVariable final Long id) {
        return ResponseEntity.ok(ticketsService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTickets(@RequestBody @Valid final TicketsDTO ticketsDTO) {
        return new ResponseEntity<>(ticketsService.create(ticketsDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTickets(@PathVariable final Long id,
            @RequestBody @Valid final TicketsDTO ticketsDTO) {
        ticketsService.update(id, ticketsDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTickets(@PathVariable final Long id) {
        ticketsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
