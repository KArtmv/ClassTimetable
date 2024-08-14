package ua.foxminded.WebProject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.DTO.NamesDto;
import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.service.*;
import ua.foxminded.WebProject.util.MyLocalDate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/lesson")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('admin', 'staff')")
public class LessonController {

    private static final String LESSONS_PER_DAY_PATH = "/lessons-per-day";
    private static final String LESSONS_PER_DAY_BY_GROUP_ID = "/lessons-by-group-{id}";
    private static final String LESSONS_PER_DAY_BY_TEACHER_ID = "/lessons-by-teacher-{id}";
    private static final String LESSONS_BETWEEN_DATES_PATH = "/lessons-between-dates";
    private static final String LESSONS_BETWEEN_DAYS_BY_GROUP_ID = "/lessons-between-dates/group-{id}";
    private static final String LESSONS_BETWEEN_DAYS_BY_TEACHER_ID = "/lessons-between-dates/teacher-{id}";

    private final MyLocalDate localDate;
    private final LessonService lessonService;
    private final GroupService groupService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;

    @GetMapping
    public String lessonPage(Model model) {
        model.addAttribute("classtables", sortLessonsByDateMap(lessonService.getAll()));
        model.addAttribute("lessonPerDayPath", LESSONS_PER_DAY_PATH);
        model.addAttribute("lessonByDatesPath", LESSONS_BETWEEN_DATES_PATH);
        return "lesson/lesson";
    }

    @GetMapping("/add-lesson")
    public String addLesson(Model model) {
        model.addAttribute("lessonDto", new LessonDto());
        model.addAllAttributes(fillModel());
        model.addAttribute("teachers", new Teacher());
        model.addAttribute("classrooms", new Classroom());
        model.addAttribute("edit", false);
        return "lesson/features/newLesson";
    }

    @GetMapping("/fetch-lessenNum")
    public ResponseEntity<List<Integer>> fetchLessonNum(@RequestParam("date") String date, @RequestParam("groupId") Long groupId) {
        return ResponseEntity.ok(lessonService.getAvailableNumbersOfLessons(LocalDate.parse(date), groupId));
    }

    @GetMapping("/fetch-teachers")
    public ResponseEntity<List<NamesDto>> fetchTeacher(@RequestParam("date") String date, @RequestParam("lessonNum") Integer lessonNum) {
        return ResponseEntity.ok(lessonService.getAvailableTeachers(LocalDate.parse(date), lessonNum));
    }

    @GetMapping("/fetch-classrooms")
    public ResponseEntity<List<Classroom>> fetchClassrooms(@RequestParam("date") String date, @RequestParam("lessonNum") Integer lessenNum) {
        return ResponseEntity.ok(lessonService.getAvailableClassrooms(LocalDate.parse(date), lessenNum));
    }

    @PostMapping("/save")
    public String save(@Valid LessonDto lessonDto, BindingResult bindingResult, Model model) {
        if (lessonDto.getDate() != null && isWeekendDay(lessonDto.getDate())) {
            bindingResult.rejectValue("date", "", "Cannot be weekend day");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("lessonDto", lessonDto);
            model.addAttribute("lessonsNum", lessonService.getAvailableNumbersOfLessons(lessonDto.getDate(), lessonDto.getGroupId()));
            model.addAttribute("teachers", lessonService.getAvailableTeachers(lessonDto.getDate(), lessonDto.getLessonNum()));
            model.addAttribute("classrooms", lessonService.getAvailableClassrooms(lessonDto.getDate(), lessonDto.getLessonNum()));
            model.addAllAttributes(fillModel());
            model.addAttribute("edit", false);
            return "lesson/features/newLesson";
        }
        lessonService.saveLesson(lessonDto);
        return "redirect:/lesson";
    }

    @GetMapping("/edit-{lessonId}")
    public String editLesson(@PathVariable Long lessonId, Model model) {
        model.addAttribute("edit", true);
        model.addAttribute("lessonDto", lessonService.getLessonDtoById(lessonId));
        model.addAttribute("lessonsNum", lessonService.getLessonsNums());
        model.addAttribute("teachers", teacherService.getAllNames());
        model.addAttribute("classrooms", classroomService.getAll());
        model.addAllAttributes(fillModel());
        return "lesson/features/newLesson";
    }

    @PostMapping("/update")
    public String update(@Valid LessonDto lessonDto, BindingResult bindingResult, Model model) {
        if (lessonDto.getDate() != null && isWeekendDay(lessonDto.getDate())) {
            bindingResult.rejectValue("date", "", "Cannot be weekend day");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("lessonDto", lessonDto);
            model.addAttribute("lessonsNum", lessonService.getAvailableNumbersOfLessons(lessonDto.getDate(), lessonDto.getGroupId()));
            model.addAttribute("teachers", lessonService.getAvailableTeachers(lessonDto.getDate(), lessonDto.getLessonNum()));
            model.addAttribute("classrooms", lessonService.getAvailableClassrooms(lessonDto.getDate(), lessonDto.getLessonNum()));
            model.addAllAttributes(fillModel());
            model.addAttribute("edit", true);
            return "lesson/features/newLesson";
        }
        lessonService.update(lessonDto);
        return "redirect:/lesson";
    }

    @GetMapping("/details-{lessonId}")
    @PreAuthorize("hasAnyRole('student', 'teacher', 'admin', 'staff')")
    public String details(@PathVariable Long lessonId, Model model) {
        model.addAttribute("lesson", lessonService.getById(lessonId));
        return "lesson/features/details";
    }

    @PreAuthorize("hasAnyRole('admin', 'staff', 'student', 'teacher')")
    @GetMapping(value = {LESSONS_BETWEEN_DATES_PATH, LESSONS_BETWEEN_DAYS_BY_GROUP_ID, LESSONS_BETWEEN_DAYS_BY_TEACHER_ID})
    public String lessonsBetweenDates(@PathVariable(required = false) Long id, Model model, HttpServletRequest request,
                                      @RequestParam(value = "start", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                      @RequestParam(value = "end", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        if (start == null || end == null || start.isBefore(localDate.getCurrentDate())) {
            start = localDate.getCurrentDate().with(DayOfWeek.MONDAY);
            end = localDate.getCurrentDate().with(DayOfWeek.FRIDAY);
        }
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        processLessonsBetweenDates(id, start, end, request.getRequestURI(), model);
        return "lesson/lesson";
    }

    @PreAuthorize("hasAnyRole('admin', 'staff', 'student', 'teacher')")
    @GetMapping(value = {LESSONS_PER_DAY_BY_GROUP_ID, LESSONS_PER_DAY_BY_TEACHER_ID, LESSONS_PER_DAY_PATH})
    public String lessonsPerDay(@PathVariable(required = false) Long id,
                                Model model,
                                HttpServletRequest request,
                                @RequestParam(value = "date", required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null || date.isBefore(localDate.getCurrentDate())) {
            date = localDate.getCurrentDate();
        }
        processLessonsPerDay(id, date, request.getRequestURI(), model);
        model.addAttribute("selectedDate", date);
        return "lesson/lesson";
    }

    @PostMapping("/delete-{id}")
    public String delete(@PathVariable Long id) {
        lessonService.delete(id);
        return "redirect:/lesson";
    }

    private void processLessonsPerDay(Long id, LocalDate date, String url, Model model) {
        if (url.contains("group")) {
            model.addAttribute("lessonPerDayPath", cutId(LESSONS_PER_DAY_BY_GROUP_ID) + id);
            model.addAttribute("lessonByDatesPath", cutId(LESSONS_BETWEEN_DAYS_BY_GROUP_ID) + id);
            model.addAttribute("classtables", sortLessonsByDateMap(lessonService.getLessonsByGroupIdPerDay(id, date)));
        } else if (url.contains("teacher")) {
            model.addAttribute("lessonPerDayPath", cutId(LESSONS_PER_DAY_BY_TEACHER_ID) + id);
            model.addAttribute("lessonByDatesPath", cutId(LESSONS_BETWEEN_DAYS_BY_TEACHER_ID) + id);
            model.addAttribute("classtables", sortLessonsByDateMap(lessonService.getLessonsByTeacherIdPerDay(id, date)));
        } else {
            model.addAttribute("lessonPerDayPath", LESSONS_PER_DAY_PATH);
            model.addAttribute("lessonByDatesPath", LESSONS_BETWEEN_DATES_PATH);
            model.addAttribute("classtables", sortLessonsByDateMap(lessonService.getLessonsPerDay(date)));
        }
    }

    private void processLessonsBetweenDates(Long id, LocalDate start, LocalDate end, String url, Model model) {
        if (url.contains("group")) {
            model.addAttribute("lessonPerDayPath", cutId(LESSONS_PER_DAY_BY_GROUP_ID) + id);
            model.addAttribute("lessonByDatesPath", cutId(LESSONS_BETWEEN_DAYS_BY_GROUP_ID) + id);
            model.addAttribute("classtables", sortLessonsByDateMap(lessonService.getLessonsByGroupIdBetweenDates(id, start, end)));
        } else if (url.contains("teacher")) {
            model.addAttribute("lessonPerDayPath", cutId(LESSONS_PER_DAY_BY_TEACHER_ID) + id);
            model.addAttribute("lessonByDatesPath", cutId(LESSONS_BETWEEN_DAYS_BY_TEACHER_ID) + id);
            model.addAttribute("classtables", sortLessonsByDateMap(lessonService.getLessonsByTeacherIdBetweenDates(id, start, end)));
        } else {
            model.addAttribute("lessonPerDayPath", LESSONS_PER_DAY_PATH);
            model.addAttribute("lessonByDatesPath", LESSONS_BETWEEN_DATES_PATH);
            model.addAttribute("classtables", sortLessonsByDateMap(lessonService.getLessonsBetweenDates(start, end)));
        }
    }

    private Map<String, Object> fillModel() {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("groups", groupService.getAll());
        modelMap.put("courses", courseService.getAll());
        return modelMap;
    }

    private static Map<LocalDate, Map<Group, List<Lesson>>> sortLessonsByDateMap(List<Lesson> list) {
        if (list == null) {
            return Collections.emptyMap();
        } else {
            Map<LocalDate, Map<Group, List<Lesson>>> groupLessonsByDateMap = new TreeMap<>(Comparator.naturalOrder());
            groupLessonsByDateMap.putAll(list.stream()
                    .collect(Collectors.groupingBy(
                            Lesson::getDate,
                            Collectors.groupingBy(Lesson::getGroup))));

            groupLessonsByDateMap.forEach((date, groupListMap)
                    -> groupListMap.forEach((group, lessonList)
                    -> lessonList.sort(Comparator.comparing(Lesson::getLessonNum))));
            return groupLessonsByDateMap;
        }
    }

    private boolean isWeekendDay(LocalDate date) {
        return date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    private String cutId(String string) {
        return string.replace("{id}", "");
    }
}


