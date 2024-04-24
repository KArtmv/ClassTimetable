package ua.foxminded.WebProject.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.service.CourseService;

@Controller
@RequestMapping("/course")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('admin', 'staff')")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public String coursePage(Model model) {
        model.addAttribute("courses", courseService.getAll());
        return "course/course";
    }
}
