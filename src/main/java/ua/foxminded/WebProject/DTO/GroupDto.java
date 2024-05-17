package ua.foxminded.WebProject.DTO;

import ua.foxminded.WebProject.persistence.entity.Group;

import java.io.Serializable;

/**
 * DTO for {@link Group}
 */
public record GroupDto(String groupName) implements Serializable {
}