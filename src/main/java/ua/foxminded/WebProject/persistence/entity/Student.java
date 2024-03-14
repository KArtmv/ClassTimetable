package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student extends BasePerson {

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(Long id) {
        super(id);
    }

    public Student(String firstName, String lastName, Group group) {
        super(firstName, lastName);
        this.group = group;
    }

    public Student(Long id, String firstName, String lastName, Group group) {
        super(id, firstName, lastName);
        this.group = group;
    }
}