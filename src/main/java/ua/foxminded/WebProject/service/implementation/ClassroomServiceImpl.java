package ua.foxminded.WebProject.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.foxminded.WebProject.exception.InvalidIdException;
import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.persistence.repository.ClassroomRepository;
import ua.foxminded.WebProject.service.ClassroomService;

import java.util.List;

@Service
@AllArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Override
    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom getById(Long id) {
        return classroomRepository.findById(id).orElseThrow(() -> new InvalidIdException("Not found given id:" + id));
    }
}
