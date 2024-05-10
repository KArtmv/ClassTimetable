package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course extends BaseEntity {
    @Column(name = "course_name", length = 50)
    @NotEmpty(message = "Course name is required.")
    @Size(min = 5, max = 50, message = "Course name must be between 5 and 50 characters.")
    private String courseName;

    @Column(name = "course_description", length = 100)
    @NotEmpty(message = "Course description is required.")
    @Size(min = 5, max = 100, message = "Course description must be between 5 and 100 characters.")
    private String courseDescription;

    public Course(Long id) {
        super(id);
    }

    public Course(Long id, String courseName, String courseDescription) {
        super(id);
        this.courseDescription = courseDescription;
        this.courseName = courseName;
    }
}
