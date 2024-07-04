package ua.foxminded.WebProject.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.TeacherRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.service.TeacherService;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
public class TeacherServiceImpl extends AbstractUserService<Teacher> implements TeacherService {

    private final LessonRepository lessonRepository;

    public TeacherServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, TeacherRepository teacherRepository, LessonRepository lessonRepository) {
        super(teacherRepository, passwordEncoder, Teacher.class, userRepository);
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<Lesson> getLessonForDay(Teacher teacher, LocalDate localDate) {
        return lessonRepository.getLessonsByTeacherAndDate(teacher, localDate);
    }

    @Override
    public List<Lesson> getLessonForWeek(Teacher teacher, LocalDate start, LocalDate end) {
        return lessonRepository.getLessonsByTeacherAndDateBetween(teacher, start, end);
    }
}
