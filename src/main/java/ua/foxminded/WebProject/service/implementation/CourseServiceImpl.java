package ua.foxminded.WebProject.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.foxminded.WebProject.exception.InvalidIdException;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.repository.CourseRepository;
import ua.foxminded.WebProject.service.CourseService;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private CourseRepository repository;

    @Override
    public List<Course> findAll() {
        return repository.findAll();
    }

    @Override
    public Course getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new InvalidIdException("Not found given id:" + id));
    }
}
