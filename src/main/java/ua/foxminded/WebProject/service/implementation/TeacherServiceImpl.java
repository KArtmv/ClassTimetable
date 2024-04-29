package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.TeacherDto;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.TeacherRepository;
import ua.foxminded.WebProject.service.TeacherService;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;

    @Override
    @Transactional
    public Teacher saveTeacher(TeacherDto teacherDto) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(teacher.getFirstName());
        teacher.setLastName(teacher.getLastName());
        return teacherRepository.save(teacher);
    }

    @Override
    @Transactional
    public void removeTeacherById(Teacher teacher) {
        teacherRepository.delete(getById(teacher.getId()));
    }

    @Override
    public List<Lesson> getLessonForDay(Teacher teacher, LocalDate localDate) {
        return lessonRepository.getLessonsByTeacherAndDate(teacher, localDate);
    }

    @Override
    public List<Lesson> getLessonForWeek(Teacher teacher, LocalDate start, LocalDate end) {
        return lessonRepository.getLessonsByTeacherAndDateBetween(teacher, start, end);
    }

    @Override
    public Teacher getById(Long id) {
        return teacherRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found teacher by given id:" + id));
    }

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }
}
