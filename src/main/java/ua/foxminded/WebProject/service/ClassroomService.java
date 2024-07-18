package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.DTO.ClassroomDto;

import java.util.List;

public interface ClassroomService extends AbstractService<Classroom, Long> {
    List<ClassroomDto> getAllClassroomsNames();
}
