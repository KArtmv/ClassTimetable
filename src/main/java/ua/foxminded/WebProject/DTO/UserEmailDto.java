package ua.foxminded.WebProject.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import ua.foxminded.WebProject.annotations.UniqueEmail;

import java.io.Serializable;

/**
 * DTO for {@link ua.foxminded.WebProject.persistence.entity.User}
 */
public record UserEmailDto(Long id,
                           @Email(message = "Please enter a valid email address.")
                           @NotEmpty(message = "Email address is required.")
                           @UniqueEmail String email) implements Serializable {
}