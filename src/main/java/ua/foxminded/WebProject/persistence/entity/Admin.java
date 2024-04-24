package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "admin")
public class Admin extends User {

    public Admin(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Admin(Long id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }
}
