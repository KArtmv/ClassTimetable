package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "teacher")
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