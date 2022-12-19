package com.bug_tracker.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByTicketId(Long ticket_id);
}
