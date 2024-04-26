package ua.foxminded.WebProject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.service.LessonService;

import java.util.Comparator;

@Controller
@RequestMapping("/lesson")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('admin', 'staff')")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public String lessonPage(Model model) {
        model.addAttribute("lessons", lessonService.getAll().stream().sorted(Comparator.comparing(Lesson::getDate)).toList());
        return "lesson/lesson";
    }
}


