package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
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
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private GroupService groupService;
    private StudentRepository repository;
    private LessonRepository lessonRepository;

    @Override
    public Student getById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found student by given id:" + id));
    }

    @Override
    @Transactional
    public Student saveStudent(StudentDto studentDto) {
        Student student = new Student();
        try {
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            student.setGroup(groupService.getById(studentDto.getGroup().getId()));
            return repository.save(student);
        } catch (EntityNotFoundException e){
            log.error("Saving new student is failed: {}", e.getMessage());
            return student;
        }
    }

    @Override
    @Transactional
    public void removeStudentById(Student student) {
        repository.delete(getById(student.getId()));
    }

    @Override
    public List<Lesson> getLessonsForDay(Student student, LocalDate localDate) {
        try {
            return lessonRepository.getLessonByGroupAndDate(student.getGroup(), localDate);
        } catch (EntityNotFoundException e) {
            log.error("failed to get lessons of student: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Lesson> getLessonsForWeek(Student student, LocalDate start, LocalDate end) {
        try {
            return lessonRepository.findByGroupAndDateBetween(student.getGroup(), start, end);
        } catch (EntityNotFoundException e) {
            log.error("failed to get lessons of student: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
