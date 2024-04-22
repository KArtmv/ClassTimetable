package ua.foxminded.WebProject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Student;

import java.io.Serializable;

/**
 * DTO for {@link Student}
 */
@RequiredArgsConstructor
@Getter
public class StudentDto implements Serializable {
    @Size(message = "@First name must be between {min} and {max} characters", min = 2, max = 15)
    @Pattern(message = "First name should consist of alphabetic characters only", regexp = "^[a-zA-Z]+$")
    @NotBlank(message = "Name is mandatory")
    private final String firstName;

    @Size(message = "@First name must be between {min} and {max} characters", min = 2, max = 15)
    @Pattern(message = "First name should consist of alphabetic characters only", regexp = "^[a-zA-Z]+$")
    @NotBlank(message = "Last name is mandatory")
    private final String lastName;
    @NotNull
    private final Group group;
}