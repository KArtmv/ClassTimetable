package ua.foxminded.WebProject.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ua.foxminded.WebProject.persistence.entity.Course;

import java.io.Serializable;

/**
 * DTO for {@link Course}
 */
@RequiredArgsConstructor
@Getter
public class CourseDto implements Serializable {
    @NotNull(message = "Should be added description.")
    private final String courseDescription;
    @NotNull(message = "the name of course is mandatory.")
    private final String courseName;
}