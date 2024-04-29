package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.StudentDto;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.StudentRepository;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.StudentService;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final GroupService groupService;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    @Override
    public Student getById(Long id) {
        return studentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found student by given id:" + id));
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional
    public Student saveStudent(StudentDto studentDto) {
        Student student = new Student();
        try {
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            student.setGroup(groupService.getById(studentDto.getGroup().getId()));
            return studentRepository.save(student);
        } catch (EntityNotFoundException e) {
            log.error("Saving new student is failed: {}", e.getMessage());
            return student;
        }
    }

    @Override
    @Transactional
    public void removeStudentById(Student student) {
        studentRepository.delete(getById(student.getId()));
    }

    @Override
    public List<Lesson> getLessonsForDay(Student student, LocalDate localDate) {
        return lessonRepository.getLessonByGroupAndDate(student.getGroup(), localDate);
    }

    @Override
    public List<Lesson> getLessonsForWeek(Student student, LocalDate start, LocalDate end) {
        return lessonRepository.findByGroupAndDateBetween(student.getGroup(), start, end);
    }
}
