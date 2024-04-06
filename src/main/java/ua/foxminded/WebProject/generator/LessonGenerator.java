package ua.foxminded.WebProject.generator;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.service.*;

import java.util.List;
import java.util.random.RandomGenerator;

@Component
@AllArgsConstructor
@Slf4j
public class LessonGenerator {

    private Integer maxLessons;
    private GroupService groupService;
    private CourseService courseService;
    private TeacherService teacherService;
    private ClassroomService classroomService;
    private LessonService lessonService;
    private RandomGenerator randomGenerator;

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
