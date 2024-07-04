package ua.foxminded.WebProject.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ua.foxminded.WebProject.annotations.AlphabeticChars;
import ua.foxminded.WebProject.persistence.entity.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserDto(Long id,
                      @NotBlank(message = "First name is required.")
                      @Size(min = 2, max = 15, message = "First name must be between 2 and 15 characters.")
                      @AlphabeticChars
                      String firstName,
                      @NotBlank(message = "Last name is required.")
                      @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters.")
                      @AlphabeticChars
                      String lastName,
                      @Email(message = "Please enter a valid email address.")
                      @NotEmpty(message = "Email address is required.")
                      String email) implements Serializable {
}