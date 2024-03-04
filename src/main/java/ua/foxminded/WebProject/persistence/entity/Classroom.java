package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "classroom")
public class Classroom extends BaseEntity {
    @Column(name = "classroom_name", length = 10)
    private String classroomName;

    public Classroom(Long id) {
        super(id);
    }
}