package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.repository.CourseRepository;
import ua.foxminded.WebProject.service.CourseService;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;

    @Override
    public List<Course> getAll() {
        return repository.findAll().stream().sorted(Comparator.comparingLong(Course::getId)).toList();
    }

    @Override
    @Transactional
    public Course saveCourse(Course course) {
        return repository.save(course);
    }

    @Override
    @Transactional
    public Course update(Course course) {
        Course existingCourse = getById(course.getId());
        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setCourseDescription(course.getCourseDescription());
        return repository.save(existingCourse);
    }

    @Override
    public Course getById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found course by given id:" + id));
    }

    @Override
    @Transactional
    public void remove(Course course) {
        repository.delete(getById(course.getId()));
    }
}
