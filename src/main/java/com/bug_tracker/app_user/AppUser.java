package com.bug_tracker.app_user;

import com.bug_tracker.comment.Comment;
import com.bug_tracker.project.Project;
import com.bug_tracker.ticket.Ticket;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class AppUser {

    //@Id
    //@Column(nullable = false, updatable = false)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(nullable = false, updatable = false, length = 24)
    private String id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "userLink")
    private Set<Comment> userLinkComments;

    @OneToMany(mappedBy = "author")
    private Set<Ticket> authorTickets;

    @OneToMany(mappedBy = "assigned")
    private Set<Ticket> assignedTickets;

    @ManyToMany(mappedBy = "allUserAppUsers")
    private Set<Project> allUserProjects;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
