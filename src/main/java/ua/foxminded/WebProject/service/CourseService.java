package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.persistence.entity.Course;

public interface CourseService extends AbstractService<Course, Long> {
    Course saveCourse(Course courseDto);

    Course update(Course course);

    void remove(Course course);
}
