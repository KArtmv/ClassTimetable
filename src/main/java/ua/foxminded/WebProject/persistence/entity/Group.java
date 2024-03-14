package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "groups")
public class Group extends BaseEntity {

    @Column(name = "group_name", length = 50)
    private String groupName;

    @OneToMany(mappedBy = "group")
    @ToString.Exclude
    private Set<Student> students = new LinkedHashSet<>();

    @OneToMany(mappedBy = "group")
    @ToString.Exclude
    private Set<Lesson> lessons = new LinkedHashSet<>();

    public Group(Long id) {
        super(id);
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group(Long id, String groupName) {
        super(id);
        this.groupName = groupName;
    }
}


