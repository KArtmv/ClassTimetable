package ua.foxminded.WebProject.DTO;

import ua.foxminded.WebProject.persistence.entity.Classroom;

import java.io.Serializable;

/**
 * DTO for {@link Classroom}
 */
public record ClassroomDto(Long id, String classroomName) implements Serializable {
}