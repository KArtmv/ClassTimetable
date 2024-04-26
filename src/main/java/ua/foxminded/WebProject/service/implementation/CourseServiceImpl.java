package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.CourseDto;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.repository.CourseRepository;
import ua.foxminded.WebProject.service.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;

    @Override
    public List<Course> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Course saveCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setCourseName(courseDto.getCourseName());
        course.setCourseDescription(courseDto.getCourseDescription());
        return repository.save(course);
    }

    @Override
    public Course getById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found course by given id:" + id));
    }
}
