package ua.foxminded.WebProject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.service.StudentService;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('admin', 'student', 'staff')")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public String studentPage(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "student/student";
    }
}
