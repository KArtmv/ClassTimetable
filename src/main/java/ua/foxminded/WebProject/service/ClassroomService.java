package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.persistence.entity.Classroom;

import java.util.List;
import java.util.Optional;

public interface ClassroomService extends AbstractService<Classroom, Long> {
    List<Classroom> findAll();
}
