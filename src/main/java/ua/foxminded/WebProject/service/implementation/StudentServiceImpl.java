package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.StudentRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.StudentService;
import ua.foxminded.WebProject.form.UserRegistrationForm;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class StudentServiceImpl extends UserServiceImpl implements StudentService {

    private final GroupService groupService;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;

    public StudentServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, GroupService groupService, StudentRepository studentRepository, LessonRepository lessonRepository) {
        super(userRepository, passwordEncoder);
        this.groupService = groupService;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
    }

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
    public StudentGroupDto getStudentGroup(Long id) {
        return studentRepository.getStudentGroup(id);
    }

    @Override
    @Transactional
    public User saveUser(UserRegistrationForm registrationForm) {
        Student student = new Student();
        student.setFirstName(registrationForm.getFirstName());
        student.setLastName(registrationForm.getLastName());
        student.setEmail(registrationForm.getEmail());
        student.setPassword(super.passwordEncoder.encode(registrationForm.getPassword()));
        return studentRepository.save(student);
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
