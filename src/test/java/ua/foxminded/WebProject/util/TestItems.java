package ua.foxminded.WebProject.util;

import ua.foxminded.WebProject.persistence.entity.*;

public class TestItems {

    private final TestData testData = new TestData();
    Lesson lesson = new Lesson();
    private Course course = new Course();
    private Group group = new Group();
    private final Classroom classroom = new Classroom();
    private final Teacher teacher = new Teacher();

    public Teacher getTeacher() {
        teacher.setId(6L);
        teacher.setFirstName("Jessica");
        teacher.setLastName("Brown");
        return teacher;
    }

    public Classroom getClassroom() {
        classroom.setId(3L);
        classroom.setClassroomName("Room 103");
        return classroom;
    }

    public Group getGroup() {
        group.setId(testData.groupId);
        group.setGroupName(testData.groupName);
        return group;
    }

    public Course getCourse() {
        course.setId(7L);
        course.setCourseName("Principles of Marketing");
        course.setCourseDescription("Learn the basics of marketing");
        return course;
    }

    public Lesson getLesson() {
        lesson.setId(2L);
        lesson.setDate(testData.getDate());
        lesson.setLessonNum(testData.lessenNum);
        lesson.setCourse(getCourse());
        lesson.setGroup(getGroup());
        lesson.setClassroom(getClassroom());
        lesson.setTeacher(getTeacher());
        return lesson;
    }
}
