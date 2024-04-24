package ua.foxminded.WebProject.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.service.GroupService;

@Controller
@RequestMapping("/group")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('admin', 'staff')")
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public String groupPage(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "group/group";
    }
}
