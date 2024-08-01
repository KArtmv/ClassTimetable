package ua.foxminded.WebProject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.StudentService;
import ua.foxminded.WebProject.util.GroupNameGenerator;

@Controller
@RequestMapping("/group")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('admin', 'staff')")
public class GroupController {
    private final GroupService groupService;
    private final GroupNameGenerator groupNameGenerator;
    private final StudentService studentService;

    @GetMapping
    public String groupPage(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "group/group";
    }

    @GetMapping("/addGroup")
    public String addGroup(Model model) {
        model.addAttribute("group", new Group(groupNameGenerator.generateGroupName()));
        return "group/features/addGroup";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Group group, Model model, BindingResult bindingResult) {
        if (groupService.getAll().stream().map(Group::getGroupName).anyMatch(group.getGroupName()::equalsIgnoreCase)) {
            bindingResult.rejectValue("groupName", "", "Group with given name: " + group.getGroupName() + " already exists");
            group.setGroupName(groupNameGenerator.generateGroupName());
            model.addAttribute("group", group);
            return "group/features/addGroup";
        }
        groupService.save(group);

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
    public String remove(Group group) {
        groupService.remove(group);
        return "redirect:/group";
    }

    @GetMapping("/students-of-group-{id}")
    public String studentsOfGroup(@PathVariable Long id, Model model) {
        model.addAttribute("users", studentService.getStudentsByGroupId(id));
        model.addAttribute("path", "student");
        model.addAttribute("user", "List of participants");
        return "user/user";
    }

    @GetMapping("/details-{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("group", groupService.getById(id));
        return "group/features/details";
    }
}
