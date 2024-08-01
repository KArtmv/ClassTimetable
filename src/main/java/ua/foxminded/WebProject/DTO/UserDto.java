package ua.foxminded.WebProject.DTO;

import jakarta.validation.constraints.*;
import ua.foxminded.WebProject.persistence.entity.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserDto(Long id,
                      @NotBlank(message = "First name is required.")
                      @Size(min = 2, max = 15, message = "First name must be between 2 and 15 characters.")
                      @Pattern(message = "To enter only available alphabetic characters", regexp = "^[a-zA-Z\\s]+")
                      String firstName,
                      @NotBlank(message = "Last name is required.")
                      @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters.")
                      @Pattern(message = "To enter only available alphabetic characters", regexp = "^[a-zA-Z\\s]+")
                      String lastName,
                      @Email(message = "Please enter a valid email address.")
                      @NotEmpty(message = "Email address is required.")
                      String email) implements Serializable {
}