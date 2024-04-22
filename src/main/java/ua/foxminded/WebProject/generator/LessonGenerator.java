package ua.foxminded.WebProject.generator;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.persistence.entity.*;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.service.*;

import java.util.List;
import java.util.random.RandomGenerator;

@Component
@RequiredArgsConstructor
@Slf4j
public class LessonGenerator {

    private final Integer maxLessons;
    private final GroupService groupService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final LessonService lessonService;
    private final RandomGenerator randomGenerator;

    @PostConstruct
    public void populateIfEmpty() {
        if (lessonService.isEmpty()) {
            populateLessonTable();
        }
    }

    private void populateLessonTable() {
        List<Group> groups = groupService.getAll();
        List<Course> courses = courseService.getAll();
        List<Teacher> teachers = teacherService.getAll();
        List<Classroom> classrooms = classroomService.getAll();

        while (lessonService.getAll().size() < maxLessons) {
            saveNewLesson(new LessonDto(
                    courses.get(randomGenerator.nextInt(courses.size())),
                    groups.get(randomGenerator.nextInt(groups.size())),
                    classrooms.get(randomGenerator.nextInt(classrooms.size())),
                    teachers.get(randomGenerator.nextInt(teachers.size()))));
        }
    }

    private void saveNewLesson(LessonDto lessonDto) {
        try {
            lessonService.saveLesson(lessonDto);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
        }
    }
}
