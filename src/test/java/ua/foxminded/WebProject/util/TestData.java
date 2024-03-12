package ua.foxminded.WebProject.util;

import ua.foxminded.WebProject.persistence.entity.*;

import java.time.LocalDate;

public class TestData {
    public Long studentId = 1L;
    public String studentFirstName = "Lucas";
    public String studentLastName = "Thompson";
    public Long groupId = 1L;
    public String groupName = "YS-27";
    public Long courseId = 1L;
    public String courseName = "Principles of Economics";
    public String courseDescription = "Learn about the fundamentals of economics";
    public Long classroomId = 1L;
    public String classroomName = "Room 101";
    public Long lessonId = 2L;
    public Integer lessenNum = 1;
    public Long teacherId = 1L;
    public String teacherFirstName = "John";
    public String teacherLastName = "Smith";
    private final Course course = new Course();
    private final Group group = new Group();
    private final Student student = new Student();
    private final Lesson lesson = new Lesson();
    private final Teacher teacher = new Teacher();

    public Student getStudent() {
        student.setFirstName(studentFirstName);
        student.setLastName(studentLastName);
        student.setGroup(getGroup());
        return student;
    }

    public Group getGroup() {
        group.setGroupName(groupName);
        return group;
    }

    public Course getCourse() {
        course.setCourseName(courseName);
        course.setCourseDescription(courseDescription);
        return course;
    }

    public LocalDate getDate(){
        return LocalDate.of(2024, 3, 11);
    }

    public Lesson getLesson() {
        lesson.setLessonNum(lessenNum);
        lesson.setDate(getDate());
        lesson.setGroup(new Group(groupId));
        lesson.setCourse(new Course(courseId));
        lesson.setClassroom(new Classroom(classroomId));
        lesson.setTeacher(new Teacher(teacherId));
        return lesson;
    }

    public Teacher getTeacher() {
        teacher.setFirstName(teacherFirstName);
        teacher.setLastName(teacherLastName);
        return teacher;
    }
}
