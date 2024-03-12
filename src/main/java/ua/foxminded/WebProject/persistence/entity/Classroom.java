package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "classroom")
public class Classroom extends BaseEntity {
    @Column(name = "classroom_name", length = 10)
    private String classroomName;

    @OneToMany(mappedBy = "classroom", orphanRemoval = true)
    private Set<Lesson> lessons = new LinkedHashSet<>();

    public Classroom(Long id) {
        super(id);
    }
}