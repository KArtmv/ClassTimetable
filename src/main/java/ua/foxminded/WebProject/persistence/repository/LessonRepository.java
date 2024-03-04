package ua.foxminded.WebProject.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Lesson;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> getLessonByGroupAndDate(Group group, LocalDate date);

    List<Lesson> findByGroupAndDateBetween(Group group, LocalDate firstWeekDay, LocalDate lastStudiedWeekDay);
}