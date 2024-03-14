package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course extends BaseEntity {
    @Column(name = "course_description", length = 100)
    private String courseDescription;

    @Column(name = "course_name", length = 50)
    private String courseName;

    public Course(Long id) {
        super(id);
    }

    public Course(Long id, String courseName, String courseDescription) {
        super(id);
        this.courseDescription = courseDescription;
        this.courseName = courseName;
    }
}