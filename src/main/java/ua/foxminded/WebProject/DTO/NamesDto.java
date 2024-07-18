package ua.foxminded.WebProject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link ua.foxminded.WebProject.persistence.entity.User}
 */
public record NamesDto(Long id,
                       @Size(message = "First name must be between 2 and 15 characters.", min = 2, max = 15)
                       @Pattern(message = "To enter only available alphabetic characters", regexp = "^[a-zA-Z\\s]+")
                       @NotBlank(message = "First name is required.")
                       String firstName,
                       @Size(message = "Last name must be between 2 and 20 characters.", min = 2, max = 20)
                       @Pattern(message = "To enter only available alphabetic characters", regexp = "^[a-zA-Z\\s]+")
                       @NotBlank(message = "Last name is required.")
                       String lastName) implements Serializable {
}