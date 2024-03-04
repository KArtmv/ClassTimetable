package ua.foxminded.WebProject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link ua.foxminded.WebProject.persistence.entity.Teacher}
 */
@AllArgsConstructor
@Getter
public class TeacherDto implements Serializable {
    @Size(min = 2, max = 15, message = "First name must be between {min} and {max} characters")
    @NotBlank(message = "First Name is mandatory")
    @Pattern(message = "First name should consist of alphabetic characters only", regexp = "^[a-zA-Z]+$")
    private final String firstName;
    @Size(min = 2, max = 15, message = "Last name must be between {min} and {max} characters")
    @Pattern(message = "Last name should consist of alphabetic characters only", regexp = "^[a-zA-Z]+$")
    @NotBlank(message = "Last name is mandatory")
    private final String lastName;
}