package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.StudentDto;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Student;

import java.time.LocalDate;
import java.util.List;

public interface StudentService extends AbstractService<Student, Long> {
    Student saveStudent(StudentDto studentDto);
    void removeStudentById(Student student);
    List<Lesson> getLessonsForDay(Student student, LocalDate localDate);
    List<Lesson> getLessonsForWeek(Student student, LocalDate start, LocalDate end);
}
