package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.TeacherDto;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Teacher;

import java.time.LocalDate;
import java.util.List;

public interface TeacherService extends AbstractService<Teacher, Long> {
    Teacher saveTeacher(TeacherDto teacherDto);
    void removeTeacherById(Teacher teacher);
    List<Teacher> findAll();
    List<Lesson> getLessonForDay(Teacher teacher, LocalDate localDate);
    List<Lesson> getLessonForWeek(Teacher teacher, LocalDate start, LocalDate end);
}
