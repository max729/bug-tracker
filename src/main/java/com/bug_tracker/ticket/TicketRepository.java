package com.bug_tracker.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
        SELECT t FROM Ticket t WHERE t.projectLink IN
        ( SELECT u.allUserProjects FROM AppUser u Where u.id = :id )
        """)
    List<Ticket> findTicketsByUserAndProjects(String id);



    @Query("""
        SELECT t FROM Ticket t LEFT JOIN FETCH t.ticketComments WHERE t.id = :id
        """)
    Optional<Ticket> findByIdWithComments (Long id);

    @Query("""
        SELECT t FROM Ticket t WHERE t.assigned.id = :id
        """)
    List<Ticket> findallByAssignedId (String id);




    
}
