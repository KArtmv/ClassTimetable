package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.DTO.StudentGroupDto;

import java.time.LocalDate;
import java.util.List;

public interface StudentService extends UserService, AbstractService<Student, Long> {
    List<Lesson> getLessonsForDay(Student student, LocalDate localDate);

    List<Lesson> getLessonsForWeek(Student student, LocalDate start, LocalDate end);

    StudentGroupDto getStudentGroup(Long id);

    Student updateStudentGroup(StudentGroupDto studentGroupDto);
}
