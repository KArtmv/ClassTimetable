package ua.foxminded.WebProject.DTO;

import ua.foxminded.WebProject.persistence.entity.Student;

import java.io.Serializable;

/**
 * DTO for {@link Student}
 */
public record StudentGroupDto(Long id, Long groupId, String groupName) implements Serializable {
}