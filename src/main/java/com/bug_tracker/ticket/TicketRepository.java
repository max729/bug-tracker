package com.bug_tracker.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
        SELECT t FROM Ticket t WHERE t.projectLink IN
        ( SELECT p.id FROM Project p JOIN AppUser u ON p.id = u.id AND u.id = :id)
        """)
    List<Ticket> findTicketsByUserAndProjects(String id);



    @Query("""
        SELECT t FROM Ticket t JOIN FETCH t.ticketComments c WHERE t.id = :id
        """)
    Optional<Ticket> findByIdWithComments (Long id);




    
}
