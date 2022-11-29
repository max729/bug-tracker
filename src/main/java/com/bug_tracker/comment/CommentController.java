package com.bug_tracker.comment;

import com.bug_tracker.app_user.AppUser;
import com.bug_tracker.app_user.AppUserRepository;
import com.bug_tracker.tickets.Tickets;
import com.bug_tracker.tickets.TicketsRepository;
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
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final AppUserRepository appUserRepository;
    private final TicketsRepository ticketsRepository;

    public CommentController(final CommentService commentService,
            final AppUserRepository appUserRepository, final TicketsRepository ticketsRepository) {
        this.commentService = commentService;
        this.appUserRepository = appUserRepository;
        this.ticketsRepository = ticketsRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userLinkValues", appUserRepository.findAll().stream().collect(
                Collectors.toMap(AppUser::getId, AppUser::getFirstName)));
        model.addAttribute("ticketValues", ticketsRepository.findAll().stream().collect(
                Collectors.toMap(Tickets::getId, Tickets::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("comments", commentService.findAll());
        return "comment/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("comment") final CommentDTO commentDTO) {
        return "comment/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("comment") @Valid final CommentDTO commentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "comment/add";
        }
        commentService.create(commentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("comment.create.success"));
        return "redirect:/comments";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("comment", commentService.get(id));
        return "comment/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("comment") @Valid final CommentDTO commentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "comment/edit";
        }
        commentService.update(id, commentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("comment.update.success"));
        return "redirect:/comments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        commentService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("comment.delete.success"));
        return "redirect:/comments";
    }

}
