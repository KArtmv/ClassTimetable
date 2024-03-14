package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.persistence.repository.ClassroomRepository;
import ua.foxminded.WebProject.service.ClassroomService;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Override
    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom getById(Long id) {
        return classroomRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found the classroom by given id:" + id));
    }
}
