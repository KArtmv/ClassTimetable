package ua.foxminded.WebProject.generator;

import lombok.RequiredArgsConstructor;
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

    public void fillLessonTable() {
        while (lessonService.getAll().size() < maxLessons) {
            saveLesson(generateLesson());
        }
    }

    private LessonDto generateLesson() {
        List<Group> groups = groupService.getAll();
        List<Course> courses = courseService.getAll();
        List<Teacher> teachers = teacherService.getAll();
        List<Classroom> classrooms = classroomService.getAll();

        return new LessonDto(
                courses.get(randomGenerator.nextInt(courses.size())),
                groups.get(randomGenerator.nextInt(groups.size())),
                classrooms.get(randomGenerator.nextInt(classrooms.size())),
                teachers.get(randomGenerator.nextInt(teachers.size())));
    }

    private void saveLesson(LessonDto lessonDto) {
        try {
            lessonService.saveLesson(lessonDto);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
        }
    }
}
