package ua.foxminded.WebProject.util;

import lombok.Getter;
import ua.foxminded.WebProject.DTO.CourseDto;
import ua.foxminded.WebProject.persistence.entity.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
public class TestData {
    private final Long studentId = 1L;
    private final String studentFirstName = "Lucas";
    private final String studentLastName = "Thompson";
    private final String studentEmail = "student@gmail.com";
    private final Student student = new Student(studentFirstName, studentLastName, getGroup());

    private final Long groupId = 1L;
    private final String groupName = "YS-27";
    private final Group group = new Group(groupName);

    private final Long courseId = 1L;
    private final String courseName = "Principles of Economics";
    private final String courseDescription = "Learn about the fundamentals of economics";
    private final Course course = new Course(courseDescription, courseName);
    private final CourseDto courseDto = new CourseDto(courseDescription, courseName);

    private final Long classroomId = 1L;
    private final String classroomName = "Room 101";

    private final Long lessonId = 2L;
    private final Integer lessenNum = 1;
    private final Lesson lesson = new Lesson(getDate(),
            lessenNum,
            new Course(getCourseId()),
            new Group(getGroupId()),
            new Classroom(getClassroomId()),
            new Teacher(getTeacherId()));

    private final Long teacherId = 1L;
    private final String teacherFirstName = "John";
    private final String teacherLastName = "Smith";
    private final String teacherEmail = "teacher@gmail.com";
    private final Teacher teacher = new Teacher(teacherFirstName, teacherLastName);

    private final String adminFirstName = "John";
    private final String adminLastName = "Thomson";
    private final String adminEmail = "admin@gmail.com";

    private final String staffFirstName = "Lucas";
    private final String staffLastName = "Smith";
    private final String staffEmail = "staff@gmail.com";

    private final String email = "test@gmail.com";

    private final LocalDate date = LocalDate.of(2024, 3, 11);

    private final LocalDate dateStudentStart = getDate().with(DayOfWeek.TUESDAY);
    private final LocalDate dateStudentEnd = getDate().with(DayOfWeek.THURSDAY);

    private final LocalDate dateTeacherStart = getDate().with(DayOfWeek.WEDNESDAY);
    private final LocalDate dateTeacherEnd = getDate().with(DayOfWeek.FRIDAY);

}
