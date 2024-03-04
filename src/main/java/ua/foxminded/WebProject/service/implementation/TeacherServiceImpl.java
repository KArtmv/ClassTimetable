package ua.foxminded.WebProject.service.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.foxminded.WebProject.DTO.TeacherDto;
import ua.foxminded.WebProject.exception.InvalidIdException;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.TeacherRepository;
import ua.foxminded.WebProject.service.LessonService;
import ua.foxminded.WebProject.service.TeacherService;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private TeacherRepository teacherRepository;
    private LessonRepository lessonRepository;
    private LessonService lessonService;

    @Override
    public Teacher saveTeacher(TeacherDto teacherDto) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(teacher.getFirstName());
        teacher.setLastName(teacher.getLastName());
        return teacherRepository.save(teacher);
    }

    @Override
    public void removeTeacherById(Teacher teacher) {
        teacherRepository.delete(getById(teacher.getId()));
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
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
        return teacherRepository.findById(id).orElseThrow(() -> new InvalidIdException("Not found given id:" + id));
    }
}
