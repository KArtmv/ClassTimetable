package ua.foxminded.WebProject.DTO;

import ua.foxminded.WebProject.persistence.entity.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserDto(Long id, String firstName, String lastName, String email) implements Serializable {
}