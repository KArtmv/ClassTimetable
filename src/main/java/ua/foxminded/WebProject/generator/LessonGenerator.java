package ua.foxminded.WebProject.generator;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Slf4j
public class LessonGenerator {

    private static final String email = "test@gmail.com";
    private static final String emailStudent = "student@gmail.com";

    private Integer maxLessons;
    private GroupService groupService;
    private CourseService courseService;
    private TeacherService teacherService;
    private ClassroomService classroomService;
    private LessonService lessonService;
    private RandomGenerator randomGenerator;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void populateIfEmpty() {
        if (lessonService.isEmpty()) {
            populateLessonTable();
        }

        if (userRepository.findUserByEmail(email).isEmpty()) {
            saveAdmin();
        }

        if (userRepository.findUserByEmail(emailStudent).isEmpty()) {
            saveStudent();
        }
    }

    private void saveStudent() {
        Student student = new Student();
        student.setEmail(emailStudent);
        student.setPassword(bCryptPasswordEncoder.encode("password"));
        student.setFirstName("Student");
        student.setLastName("Student");
        userRepository.save(student);
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

    private void saveAdmin() {
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(bCryptPasswordEncoder.encode("password"));
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        userRepository.save(admin);
    }
}
