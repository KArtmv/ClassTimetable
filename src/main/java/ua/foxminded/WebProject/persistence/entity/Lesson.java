package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson extends BaseEntity {

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "lesson_num")
    private Integer lessonNum;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Lesson(Long id) {
        super(id);
    }
}