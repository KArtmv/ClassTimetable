package ua.foxminded.WebProject.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.StudentRepository;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.StudentService;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class StudentServiceImpl extends AbstractUserService<Student> implements StudentService {

    private final GroupService groupService;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    public StudentServiceImpl(BCryptPasswordEncoder passwordEncoder, GroupService groupService, LessonRepository lessonRepository, StudentRepository studentRepository) {
        super(studentRepository, passwordEncoder, Student.class);
        this.groupService = groupService;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentGroupDto getStudentGroup(Long id) {
        return studentRepository.getStudentGroup(id);
    }

    @Override
    @Transactional
    public Student updateStudentGroup(StudentGroupDto studentGroupDto) {
        Student student = getById(studentGroupDto.id());
        student.setGroup(groupService.getById(studentGroupDto.groupId()));
        return studentRepository.save(student);
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
