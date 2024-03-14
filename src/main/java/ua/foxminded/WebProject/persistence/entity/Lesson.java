package ua.foxminded.WebProject.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Lesson(Long id) {
        super(id);
    }

    public Lesson(Long id, LocalDate date, Integer lessonNum, Course course, Group group, Classroom classroom, Teacher teacher) {
        super(id);
        this.date = date;
        this.lessonNum = lessonNum;
        this.course = course;
        this.group = group;
        this.classroom = classroom;
        this.teacher = teacher;
    }
}