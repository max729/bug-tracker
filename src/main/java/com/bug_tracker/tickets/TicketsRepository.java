package com.bug_tracker.tickets;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface TicketsRepository extends JpaRepository<Tickets, Long> {

    @Query("""
        SELECT t FROM Tickets t WHERE t.projektLink IN 
        ( SELECT p.id FROM Projekt p JOIN AppUser u ON p.id = u.id AND u.id = :id)
        """)
    List<Tickets> findTicketsByUserProjekts(Long id);

    
}
