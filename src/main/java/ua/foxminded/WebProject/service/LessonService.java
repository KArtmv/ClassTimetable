package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.DTO.NamesDto;
import ua.foxminded.WebProject.persistence.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface LessonService extends AbstractService<Lesson, Long> {
    Lesson saveLesson(LessonDto lesson);

    List<Integer> getAvailableNumbersOfLessons(LocalDate date, Long groupId);

    List<NamesDto> getAvailableTeachers(LocalDate date, Integer lessonNum);

    List<Classroom> getAvailableClassrooms(LocalDate date, Integer lessonNum);

    LessonDto getLessonDtoById(Long id);

    List<Integer> getLessonsNums();

    void update(LessonDto lessonDto);

    List<Lesson> getLessonsByGroupIdPerDay(Long groupId, LocalDate date);

    List<Lesson> getLessonsByGroupIdPerWeek(Long groupId, LocalDate start, LocalDate end);

    List<Lesson> getLessonsByTeacherIdPerDay(Long teacherId, LocalDate localDate);

    List<Lesson> getLessonsByTeacherIdPerWeek(Long teacherId, LocalDate start, LocalDate end);

    List<Lesson> getLessonsPerDay(LocalDate date);

    List<Lesson> getLessonsBetweenDates(LocalDate start, LocalDate end);

    void delete(Long id);
}
