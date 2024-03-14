package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "teacher")
public class Teacher extends BasePerson {

    @OneToMany(mappedBy = "teacher", orphanRemoval = true)
    private Set<Lesson> lessons = new LinkedHashSet<>();

    public Teacher(Long id) {
        super(id);
    }

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Teacher(Long id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }
}