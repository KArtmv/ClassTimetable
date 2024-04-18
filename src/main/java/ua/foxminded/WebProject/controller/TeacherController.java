package ua.foxminded.WebProject.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.service.TeacherService;

@Controller
@RequestMapping("/teacher")
@AllArgsConstructor
@Secured("teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public String teacherPage(Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        return "teacher/teacher";
    }
}
