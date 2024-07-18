package ua.foxminded.WebProject.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.WebProject.persistence.entity.Lesson;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Lesson}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonDto implements Serializable {
    private Long id;

    @NotNull(message = "Date is required")
    @Future(message = "Date should be after today")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull(message = "Lesson number can be empty")
    private Integer lessonNum;

    @NotNull(message = "Course is required")
    private Long courseId;
    private String courseCourseName;

    @NotNull(message = "Group is required")
    private Long groupId;
    private  String groupGroupName;

    @NotNull(message = "Classroom is required")
    private Long classroomId;
    private String classroomClassroomName;

    @NotNull(message = "Teacher is required")
    private Long teacherId;
    private String teacherFirstName;
    private String teacherLastName;
}