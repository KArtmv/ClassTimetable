package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.DTO.CourseDto;

import java.util.List;

public interface CourseService extends AbstractService<Course, Long> {
    List<Course> findAll();
    Course saveCourse(CourseDto courseDto);
}
