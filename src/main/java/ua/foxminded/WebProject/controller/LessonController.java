package ua.foxminded.WebProject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.DTO.NamesDto;
import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.DTO.ClassroomDto;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.service.*;
import ua.foxminded.WebProject.util.MyLocalDate;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/lesson")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('admin', 'staff')")
public class LessonController {

    private final LessonService lessonService;
    private final GroupService groupService;
    private final CourseService courseService;

    @GetMapping
    public String lessonPage(Model model) {
        model.addAttribute("lessons", lessonService.getAll().stream().sorted(Comparator.comparing(Lesson::getDate)).toList());
        return "lesson/lesson";
    }

    @GetMapping("/add-lesson")
    public String addLesson(Model model) {
        model.addAttribute("lessonDto", new LessonDto());
        model.addAllAttributes(fillModel());
        model.addAttribute("teachers", new Teacher());
        model.addAttribute("classrooms", new Classroom());
        return "lesson/features/newLesson";
    }

    @GetMapping("/fetch-lessenNum")
    public ResponseEntity<List<Integer>> fetchLessonNum(@RequestParam("date") String date, @RequestParam("groupId") Long groupId) {
        return ResponseEntity.ok(lessonService.getAllLessonsNumberByGroupAndDate(LocalDate.parse(date), groupId));
    }

    @GetMapping("/fetch-teachers")
    public ResponseEntity<List<NamesDto>> fetchTeacher(@RequestParam("date") String date, @RequestParam("lessonNum") Integer lessonNum) {
        return ResponseEntity.ok(lessonService.getAvailableTeachers(LocalDate.parse(date), lessonNum));
    }

    @GetMapping("/fetch-classrooms")
    public ResponseEntity<List<ClassroomDto>> fetchClassrooms(@RequestParam("date") String date, @RequestParam("lessonNum") Integer lessenNum) {
        return ResponseEntity.ok(lessonService.getAvailableClassrooms(LocalDate.parse(date), lessenNum));
    }

    @PostMapping("/save")
    public String save(@Valid LessonDto lessonDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("lessonDto", lessonDto);
            model.addAttribute("lessons", lessonService.getAllLessonsNumberByGroupAndDate(lessonDto.getDate(), lessonDto.getGroupId()));
            model.addAttribute("teachers", lessonService.getAvailableTeachers(lessonDto.getDate(), lessonDto.getLessonNum()));
            model.addAttribute("classrooms", lessonService.getAvailableClassrooms(lessonDto.getDate(), lessonDto.getLessonNum()));
            model.addAllAttributes(fillModel());
            return "lesson/features/newLesson";
        }
        lessonService.saveLesson(lessonDto);
        return "redirect:/lesson";
    }

    private Map<String, Object> fillModel() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("groups", groupService.getAll());
        modelMap.put("courses", courseService.getAll());
        return modelMap;
    }
}


