package ua.foxminded.WebProject.service.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.foxminded.WebProject.DTO.StudentDto;
import ua.foxminded.WebProject.exception.InvalidIdException;
import ua.foxminded.WebProject.persistence.entity.Group;
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
public class StudentServiceImpl implements StudentService {

    private GroupService groupService;
    private StudentRepository repository;
    private LessonRepository lessonRepository;

    @Override
    public Student getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new InvalidIdException("Not found given id:" + id));
    }

    @Override
    public Student saveStudent(StudentDto studentDto) {
        Student finality = new Student();
        try {
            finality.setGroup(groupService.getById(studentDto.getGroup().getId()));
            return repository.save(finality);
        } catch (InvalidIdException e){
            log.error("Saving new student is failed: {}", e.getMessage());
            return finality;
        }
    }

    @Override
    public void removeStudentById(Student student) {
        repository.delete(getById(student.getId()));
    }

    @Override
    public List<Lesson> getLessonsForDay(Student student, LocalDate localDate) {
        try {
            Group group = groupService.getById(student.getGroup().getId());
            return lessonRepository.getLessonByGroupAndDate(group, localDate);
        } catch (InvalidIdException e) {
            log.error("failed to get lessons of student: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Lesson> getLessonsForWeek(Student student, LocalDate start, LocalDate end) {
        try {
            Group group = groupService.getById(student.getGroup().getId());
            return lessonRepository.findByGroupAndDateBetween(group, start, end);
        } catch (InvalidIdException e) {
            log.error("failed to get lessons of student: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
