package ua.foxminded.WebProject.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.persistence.repository.StudentRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.StudentService;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class StudentServiceImpl extends AbstractUserService<Student> implements StudentService {

    private final GroupService groupService;
    private final StudentRepository studentRepository;

    public StudentServiceImpl(BCryptPasswordEncoder passwordEncoder, GroupService groupService, StudentRepository studentRepository, UserRepository userRepository) {
        super(studentRepository, passwordEncoder, Student.class, userRepository);
        this.groupService = groupService;
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
    public List<UserDto> getStudentsByGroupId(Long groupId) {
        return studentRepository.getAllByGroupId(groupId);
    }
}
