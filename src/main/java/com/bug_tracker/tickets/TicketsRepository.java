package com.bug_tracker.tickets;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketsRepository extends JpaRepository<Tickets, Long> {
}
