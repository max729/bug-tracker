package com.bug_tracker.projekt;

import com.bug_tracker.app_user.AppUser;
import com.bug_tracker.app_user.AppUserRepository;
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
@RequestMapping("/projekts")
public class ProjektController {

    private final ProjektService projektService;
    private final AppUserRepository appUserRepository;

    public ProjektController(final ProjektService projektService,
            final AppUserRepository appUserRepository) {
        this.projektService = projektService;
        this.appUserRepository = appUserRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("allUsersValues", appUserRepository.findAll().stream().collect(
                Collectors.toMap(AppUser::getId, AppUser::getFirstName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("projekts", projektService.findAll());
        return "projekt/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("projekt") final ProjektDTO projektDTO) {
        return "projekt/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("projekt") @Valid final ProjektDTO projektDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("projektName") && projektService.projektNameExists(projektDTO.getProjektName())) {
            bindingResult.rejectValue("projektName", "Exists.projekt.projektName");
        }
        if (bindingResult.hasErrors()) {
            return "projekt/add";
        }
        projektService.create(projektDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("projekt.create.success"));
        return "redirect:/projekts";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("projekt", projektService.get(id));
        return "projekt/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("projekt") @Valid final ProjektDTO projektDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("projektName") &&
                !projektService.get(id).getProjektName().equalsIgnoreCase(projektDTO.getProjektName()) &&
                projektService.projektNameExists(projektDTO.getProjektName())) {
            bindingResult.rejectValue("projektName", "Exists.projekt.projektName");
        }
        if (bindingResult.hasErrors()) {
            return "projekt/edit";
        }
        projektService.update(id, projektDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("projekt.update.success"));
        return "redirect:/projekts";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = projektService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            projektService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("projekt.delete.success"));
        }
        return "redirect:/projekts";
    }

}
