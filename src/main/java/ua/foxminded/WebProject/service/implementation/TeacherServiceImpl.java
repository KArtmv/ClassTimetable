package ua.foxminded.WebProject.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.persistence.repository.TeacherRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.service.TeacherService;

@Service
@Slf4j
@Transactional(readOnly = true)
public class TeacherServiceImpl extends AbstractUserService<Teacher> implements TeacherService {


    public TeacherServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, TeacherRepository teacherRepository) {
        super(teacherRepository, passwordEncoder, Teacher.class, userRepository);
    }
}
