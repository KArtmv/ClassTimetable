package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.service.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class LessonServiceImpl implements LessonService {

    private static final int MAX_AMOUNT_LESSONS_BY_COURSE_PER_WEEK = 3;
    private static final int MAX_AMOUNT_LESSONS_PER_DAY = 8;

    private final LessonRepository repository;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final ClassroomService classroomService;

    @Override
    @Transactional
    public Lesson saveLesson(LessonDto lessonDto){
        Lesson lesson = new Lesson();
        try{
            lesson = verifyId(lessonDto);
            lesson = findAvailableWeek(lesson);
        } catch (EntityNotFoundException | DataIntegrityViolationException e){
            log.error("Failed adding lesson: {}", e.getMessage());
        }
        return lesson;
    }

    @Override
    public List<Lesson> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Lesson> getAllLessonsPerDay(LocalDate date) {
        return repository.getAllByDate(date);
    }

    @Override
    public Lesson getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found given id:" + id));
    }

    @Override
    public List<Student> getParticipantsOfLesson(Lesson lessonId) {
        try {
            Lesson lesson = getById(lessonId.getId());
            return lesson.getGroup().getStudents().stream().toList();
        } catch (EntityNotFoundException e) {
            log.error("Failed to get participants of lessons: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private Lesson verifyId(LessonDto lessonDto) {
        Lesson lesson = new Lesson();
        lesson.setGroup(groupService.getById(lessonDto.group().getId()));
        lesson.setCourse(courseService.getById(lessonDto.course().getId()));
        lesson.setClassroom(classroomService.getById(lessonDto.classroom().getId()));
        lesson.setTeacher(teacherService.getById(lessonDto.teacher().getId()));
        return lesson;
    }

    private Lesson findAvailableWeek(Lesson lesson){
        Group group = lesson.getGroup();
        Course course = lesson.getCourse();
        LocalDate date = LocalDate.now();
        int attempts = 0;
        int maxAttempts = 3;
        LocalDate monday;

        do {
            monday = date.with(DayOfWeek.MONDAY);
            if (attempts > 0){
                date = monday;
            }

            if (repository.findByGroupAndDateBetween(group, monday, date.with(DayOfWeek.FRIDAY)).stream()
                    .filter(l -> l.getCourse().getId().equals(course.getId())).count() < MAX_AMOUNT_LESSONS_BY_COURSE_PER_WEEK){

                lesson = addLessonToAvailableDay(group, date, lesson.getCourse(), lesson);
                if (lesson.getId() != null){
                    return lesson;
                }
            }
            date = date.plusWeeks(1);
            attempts++;
        } while (attempts < maxAttempts);
        return lesson;
    }

    private Lesson addLessonToAvailableDay(Group group, LocalDate date, Course course, Lesson lesson){
        do {
                List<Lesson> lessonsPerDay = repository.getLessonByGroupAndDate(group, date);

                if (lessonsPerDay.size() < MAX_AMOUNT_LESSONS_PER_DAY && lessonsPerDay.stream().noneMatch(l -> l.getCourse().getId().equals(course.getId()))) {
                    lesson.setLessonNum(lessonsPerDay.isEmpty() ? 1 : lessonsPerDay.size() + 1);
                    lesson.setDate(date);

                    lesson = repository.save(lesson);
                    if (lesson.getId() != null) {
                        return lesson;
                    }
                }
            date = date.plusDays(1);
        } while (date.getDayOfWeek().getValue() < DayOfWeek.SATURDAY.getValue());
        return lesson;
    }
}
