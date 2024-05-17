package ua.foxminded.WebProject.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.WebProject.DTO.GroupDto;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.util.GroupNameGenerator;

@Controller
@RequestMapping("/group")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('admin', 'staff')")
public class GroupController {
    private final GroupService groupService;
    private final GroupNameGenerator groupNameGenerator;

    @GetMapping
    public String groupPage(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "group/group";
    }

    @GetMapping("/addGroup")
    public String addGroup(Model model) {
        model.addAttribute("duplicate", false);
        model.addAttribute("groupDto", new GroupDto(groupNameGenerator.generateGroupName()));
        return "group/features/addGroup";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute GroupDto groupDto, Model model) {
        if (groupService.saveGroup(groupDto).getId() == null){
            model.addAttribute("duplicate", true);
            return "group/features/addGroup";
        }
        return "redirect:/group";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public String deleteGroupConfirmationForm(@PathVariable Long id, Model model) {
        model.addAttribute("group", groupService.getById(id));
        return "group/features/deleteGroup";
    }

    @PostMapping("/remove")
    @PreAuthorize("hasRole('admin')")
    public String remove(Group group){
        groupService.remove(group);
        return "redirect:/group";
    }
}
