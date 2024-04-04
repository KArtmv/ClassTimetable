package ua.foxminded.WebProject.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.service.*;

import java.util.Comparator;

@Controller
@AllArgsConstructor
public class AppController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final CourseService courseService;
    private final GroupService groupService;

    @GetMapping("/student")
    public String studentPage(Model model){
        model.addAttribute("students", studentService.getAll());
        return "student/student";
    }

    @GetMapping("/teacher")
    public String teacherPage(Model model){
        model.addAttribute("teachers", teacherService.findAll());
        return "teacher/teacher";
    }

    @GetMapping("/lesson")
    public String lessonPage(Model model){
        model.addAttribute("lessons", lessonService.getAll().stream().sorted(Comparator.comparing(Lesson::getDate)));
        return "lesson/lesson";
    }

    @GetMapping("/course")
    public String coursePage(Model model){
        model.addAttribute("courses", courseService.getAll());
        return "course/course";
    }

    @GetMapping("/group")
    public String groupPage(Model model){
        model.addAttribute("groups", groupService.getAll());
        return "group/group";
    }
}
