package ua.foxminded.WebProject.DTO;

import jakarta.validation.constraints.NotNull;
import ua.foxminded.WebProject.persistence.entity.*;

import java.io.Serializable;

/**
 * DTO for {@link Lesson}
 */

public record LessonDto(@NotNull(message = "Course can not be empty") Course course,
                        @NotNull(message = "Group can not be empty") Group group,
                        @NotNull(message = "Classroom can not be empty") Classroom classroom,
                        @NotNull(message = "Teacher can not be empty.") Teacher teacher) implements Serializable {
}