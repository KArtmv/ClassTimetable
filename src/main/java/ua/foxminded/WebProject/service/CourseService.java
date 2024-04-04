package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.CourseDto;
import ua.foxminded.WebProject.persistence.entity.Course;

public interface CourseService extends AbstractService<Course, Long> {
    Course saveCourse(CourseDto courseDto);
}
