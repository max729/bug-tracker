package com.bug_tracker.tickets;

import com.bug_tracker.app_user.AppUser;
import com.bug_tracker.app_user.AppUserRepository;
import com.bug_tracker.projekt.Projekt;
import com.bug_tracker.projekt.ProjektRepository;
import com.bug_tracker.util.WebUtils;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/ticketss")
public class TicketsController {

    private final TicketsService ticketsService;
    private final ProjektRepository projektRepository;
    private final AppUserRepository appUserRepository;

    public TicketsController(final TicketsService ticketsService,
            final ProjektRepository projektRepository, final AppUserRepository appUserRepository) {
        this.ticketsService = ticketsService;
        this.projektRepository = projektRepository;
        this.appUserRepository = appUserRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("statusValues", TicketStatus.values());
        model.addAttribute("priorityValues", Priority.values());
        model.addAttribute("typValues", TicketType.values());
        model.addAttribute("projektLinkValues", projektRepository.findAll().stream().collect(
                Collectors.toMap(Projekt::getId, Projekt::getProjektName)));
        model.addAttribute("authorValues", appUserRepository.findAll().stream().collect(
                Collectors.toMap(AppUser::getId, AppUser::getFirstName)));
        model.addAttribute("assignedValues", appUserRepository.findAll().stream().collect(
                Collectors.toMap(AppUser::getId, AppUser::getFirstName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("ticketss", ticketsService.findAll());
        return "tickets/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("tickets") final TicketsDTO ticketsDTO) {
        return "tickets/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("tickets") @Valid final TicketsDTO ticketsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tickets/add";
        }
        ticketsService.create(ticketsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("tickets.create.success"));
        return "redirect:/ticketss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("tickets", ticketsService.get(id));
        return "tickets/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("tickets") @Valid final TicketsDTO ticketsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "tickets/edit";
        }
        ticketsService.update(id, ticketsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("tickets.update.success"));
        return "redirect:/ticketss";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = ticketsService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            ticketsService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("tickets.delete.success"));
        }
        return "redirect:/ticketss";
    }

}
