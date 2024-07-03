package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(value = "staff")
@NoArgsConstructor
public class Staff extends User {
}
