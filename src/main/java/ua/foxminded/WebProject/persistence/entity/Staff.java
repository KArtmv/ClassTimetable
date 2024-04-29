package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = "staff")
@NoArgsConstructor
public class Staff extends User {

    public Staff(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Staff(Long id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }
}
