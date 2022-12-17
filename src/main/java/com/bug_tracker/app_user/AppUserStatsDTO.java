package com.bug_tracker.app_user;

import java.util.EnumMap;


import com.bug_tracker.tickets.Priority;
import com.bug_tracker.tickets.TicketStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserStatsDTO {
    //Integer TotalProjects;  
    Integer totalTicktets;
    EnumMap<Priority, Integer> totalTicktetsByPriority;
    EnumMap<TicketStatus, Integer> totalTicktetsByStatus;
 
}

