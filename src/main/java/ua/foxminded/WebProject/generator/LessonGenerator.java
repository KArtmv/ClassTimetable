package ua.foxminded.WebProject.generator;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.service.*;

import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

@Component
@AllArgsConstructor
@Slf4j
public class LessonGenerator {

    private GroupService groupService;
    private CourseService courseService;
    private TeacherService teacherService;
    private ClassroomService classroomService;
    private LessonService lessonService;

    @PostConstruct
    public void populateIfEmpty(){
        if (lessonService.isEmpty()){
            populateLessonTable();
        }
    }

    public void populateLessonTable(){
        List<Group> groups = groupService.getAll();
        List<Course> courses = courseService.getAll();
        List<Teacher> teachers = teacherService.findAll();
        List<Classroom> classrooms = classroomService.getAll();

        RandomGenerator randomGenerator  = new Random();

        while (lessonService.getAll().size() < 350) {
            saveNewLesson(new LessonDto(
                    courses.get(randomGenerator.nextInt(courses.size())),
                    groups.get(randomGenerator.nextInt(groups.size())),
                    classrooms.get(randomGenerator.nextInt(classrooms.size())),
                    teachers.get(randomGenerator.nextInt(teachers.size()))));
        }
    }

    public void saveNewLesson(LessonDto lessonDto){
        try{
            lessonService.saveLesson(lessonDto);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            log.error(e.getMessage());
        }

    }
}
