package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.repository.CourseRepository;
import ua.foxminded.WebProject.service.CourseService;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private CourseRepository repository;

    @Override
    public List<Course> findAll() {
        return repository.findAll();
    }

    @Override
    public Course getById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found course by given id:" + id));
    }
}
