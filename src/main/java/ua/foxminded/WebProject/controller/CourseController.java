package ua.foxminded.WebProject.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.service.CourseService;

@Controller
@RequestMapping("/course")
@AllArgsConstructor
@Secured("admin")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public String coursePage(Model model) {
        model.addAttribute("courses", courseService.getAll());
        return "course/course";
    }
}
