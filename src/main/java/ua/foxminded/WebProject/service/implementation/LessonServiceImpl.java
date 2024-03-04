package ua.foxminded.WebProject.service.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.exception.InvalidIdException;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.service.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class LessonServiceImpl implements LessonService {

    private static final int MAX_AMOUNT_LESSONS_BY_COURSE_PER_WEEK = 3;
    private static final int MAX_AMOUNT_LESSONS_PER_DAY = 8;

    private final LessonRepository repository;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final ClassroomService classroomService;

    @Override
    public Lesson saveLesson(LessonDto lesson){
        Lesson finality = new Lesson();
        try{
            finality = verifyId(lesson);
            finality = findAvailableWeek(finality);
        } catch (InvalidIdException | DataIntegrityViolationException e){
            log.error("Failed adding lesson: {}", e.getMessage());
        }
        return finality;
    }

    @Override
    public List<Lesson> getAll() {
        return repository.findAll();
    }

    @Override
    public Lesson getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new InvalidIdException("Not found given id:" + id));
    }

    private Lesson verifyId(LessonDto lesson) {
        Lesson finality = new Lesson();
        finality.setGroup(groupService.getById(lesson.group().getId()));
        finality.setCourse(courseService.getById(lesson.course().getId()));
        finality.setClassroom(classroomService.getById(lesson.classroom().getId()));
        finality.setTeacher(teacherService.getById(lesson.teacher().getId()));
        return finality;
    }

    private Lesson findAvailableWeek(Lesson lesson){
        Group group = lesson.getGroup();
        LocalDate date = LocalDate.now();
        int attempts = 0;
        int maxAttempts = 3;
        LocalDate monday;

        do {
            monday = date.with(DayOfWeek.MONDAY);
            if (attempts > 0){
                date = monday;
            }

            Lesson finalLesson = lesson;
            if (repository.findByGroupAndDateBetween(group, monday, date.with(DayOfWeek.FRIDAY)).stream()
                    .filter(l -> l.getCourse().getId().equals(finalLesson.getCourse().getId())).count() < MAX_AMOUNT_LESSONS_BY_COURSE_PER_WEEK){

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

    private Lesson addLessonToAvailableDay(Group group, LocalDate date, Course course, Lesson finality){
        do {
                List<Lesson> lessonsPerDay = repository.getLessonByGroupAndDate(group, date);

                if (lessonsPerDay.size() < MAX_AMOUNT_LESSONS_PER_DAY && lessonsPerDay.stream().noneMatch(l -> l.getCourse().getId().equals(course.getId()))) {
                    finality.setLessonNum(lessonsPerDay.isEmpty() ? 1 : lessonsPerDay.size() + 1);
                    finality.setDate(date);

                    finality = repository.save(finality);
                    if (finality.getId() != null) {
                        return finality;
                    }
                }
            date = date.plusDays(1);
        } while (date.getDayOfWeek().getValue() < DayOfWeek.SATURDAY.getValue());
        return finality;
    }
}
