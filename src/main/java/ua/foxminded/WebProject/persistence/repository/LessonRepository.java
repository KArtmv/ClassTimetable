package ua.foxminded.WebProject.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Teacher;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> getLessonByGroupIdAndDate(Long groupId, LocalDate date);

    List<Lesson> getLessonsByGroupIdAndDateBetween(Long groupId, LocalDate start, LocalDate ent);

    List<Lesson> getLessonsByTeacherIdAndDate(Long teacherId, LocalDate date);

    List<Lesson> getLessonsByTeacherIdAndDateBetween(Long teacherId, LocalDate start, LocalDate end);

    List<Lesson> getAllByDate(LocalDate localDate);

    List<Lesson> getLessonsByDateBetween(LocalDate start, LocalDate end);

    List<Lesson> getLessonsByDateAndLessonNum(LocalDate date, Integer lessonNum);

    @Query("select new ua.foxminded.WebProject.DTO.LessonDto(l.id, l.date, l.lessonNum, c.id, g.id, cr.id, t.id) " +
            "from Lesson l " +
            "join l.group g " +
            "join l.course c " +
            "join l.teacher t " +
            "join l.classroom cr " +
            "where l.id = ?1")
    LessonDto getLessonDto(Long id);

}