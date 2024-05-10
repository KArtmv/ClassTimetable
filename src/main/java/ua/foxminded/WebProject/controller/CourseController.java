package ua.foxminded.WebProject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.persistence.entity.Course;
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

    @GetMapping("/addCourse")
    public String addCourseForm(Model model, Course course) {
        model.addAttribute("course", course);
        return "course/features/addCourse";
    }

    @PostMapping("/save")
    public String saveCourse(@Valid Course course, BindingResult result) {
        if (result.hasErrors()) {
            return "course/features/addCourse";
        }
        courseService.saveCourse(course);
        return "redirect:/course";
    }

    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("course", courseService.getById(id));
        return "course/features/editCourse";
    }

    @PostMapping("/update")
    public String updateCourse(@Valid Course course, BindingResult result) {
        if (result.hasErrors()) {
            return "course/features/editCourse";
        }
        courseService.update(course);
        return "redirect:/course";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public String deleteCourseForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("course", courseService.getById(id));
        return "course/features/deleteCourse";
    }

    @PostMapping("/remove")
    @PreAuthorize("hasRole('admin')")
    public String removeCourse(Course course) {
        courseService.remove(course);
        return "redirect:/course";
    }
}
