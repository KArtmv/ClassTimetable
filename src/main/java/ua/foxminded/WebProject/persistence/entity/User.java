package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(name = "first_name", length = 20)
    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 15, message = "First name must be between 2 and 15 characters.")
    @Pattern(message = "To enter only available alphabetic characters", regexp = "^[a-zA-Z\\s]+")
    private String firstName;

    @Column(name = "last_name", length = 20)
    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters.")
    @Pattern(message = "To enter only available alphabetic characters", regexp = "^[a-zA-Z\\s]+")
    private String lastName;

    @Column(name = "email", unique = true)
    @Email(message = "Please enter a valid email address.")
    @NotEmpty(message = "Email address is required.")
    private String email;

    @Column(name = "password", length = 128)
    @NotEmpty(message = "Password is required.")
    private String password;

    @Transient
    public String getRole() {
        return getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    public User(Long id) {
        super(id);
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(Long id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}


