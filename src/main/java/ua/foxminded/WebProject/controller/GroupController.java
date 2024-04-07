package ua.foxminded.WebProject.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.service.GroupService;

@Controller
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public String groupPage(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "group/group";
    }
}
