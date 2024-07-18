package ua.foxminded.WebProject.generator;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.persistence.entity.*;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.service.*;
import ua.foxminded.WebProject.util.MyLocalDate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.random.RandomGenerator;

@Component
@RequiredArgsConstructor
@Slf4j
public class LessonGenerator {

    private static final int MAX_AMOUNT_LESSONS_BY_COURSE_PER_WEEK = 3;
    private static final int MAX_AMOUNT_LESSONS_PER_DAY = 8;

    private final Integer maxLessons;
    private final GroupService groupService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final LessonService lessonService;
    private final LessonRepository lessonRepository;
    private final RandomGenerator randomGenerator;
    private final MyLocalDate localDate;


    public void fillLessonTable() {
        int groupsSize = groupService.getAll().size();
        int coursesSize = courseService.getAll().size();
        int teachersSize = teacherService.getAll().size();
        int classroomsSize = classroomService.getAll().size();

        while (lessonService.getAll().size() < maxLessons) {
            saveLesson(generateLesson(coursesSize, groupsSize, teachersSize, classroomsSize));
        }
    }

    private LessonDto generateLesson(Integer coursesSize, Integer teachersSize, Integer classroomsSize, Integer groupsSize) {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setCourseId(randomGenerator.nextLong(coursesSize));
        lessonDto.setTeacherId(randomGenerator.nextLong(teachersSize));
        lessonDto.setClassroomId(randomGenerator.nextLong(classroomsSize));
        lessonDto.setGroupId(randomGenerator.nextLong(groupsSize));

        return lessonDto;
    }

    private Lesson verifyId(LessonDto lessonDto) {
        Lesson lesson = new Lesson();
        lesson.setGroup(groupService.getById(lessonDto.getGroupId()));
        lesson.setCourse(courseService.getById(lessonDto.getCourseId()));
        lesson.setClassroom(classroomService.getById(lessonDto.getClassroomId()));
        lesson.setTeacher(teacherService.getById(lessonDto.getTeacherId()));
        return lesson;
    }

    private void saveLesson(LessonDto lessonDto) {
        try {
            saveToAvailableDate(verifyId(lessonDto));
        } catch (EntityNotFoundException e) {
            log.error("Failed adding lesson: {}", e.getMessage());
        }
    }

        private void saveToAvailableDate(Lesson lesson) {
        Group group = lesson.getGroup();
        Course course = lesson.getCourse();
        LocalDate date = localDate.getCurrentDate();
        date = date.isEqual(date.with(DayOfWeek.SATURDAY)) || date.isEqual(date.with(DayOfWeek.SUNDAY)) ? date.plusWeeks(1).with(DayOfWeek.MONDAY) : date;
        int attempts = 0;
        int maxAttempts = 3;
        LocalDate monday;

        do {
            monday = date.with(DayOfWeek.MONDAY);
            if (attempts > 0) {
                date = monday;
            }

            if (lessonRepository.findByGroupAndDateBetween(group, monday, date.with(DayOfWeek.FRIDAY)).stream()
                    .filter(l -> l.getCourse().getId().equals(course.getId())).count() < MAX_AMOUNT_LESSONS_BY_COURSE_PER_WEEK) {
                addLessonToAvailableDay(group, date, lesson.getCourse(), lesson);
            }
            date = date.plusWeeks(1);
            attempts++;
        } while (attempts < maxAttempts);
        }

    private void addLessonToAvailableDay(Group group, LocalDate date, Course course, Lesson lesson) {
        do {
            List<Lesson> lessonsPerDay = lessonRepository.getLessonByGroupAndDate(group, date);

            if (lessonsPerDay.size() < MAX_AMOUNT_LESSONS_PER_DAY && lessonsPerDay.stream().noneMatch(l -> l.getCourse().getId().equals(course.getId()))) {
                lesson.setLessonNum(lessonsPerDay.isEmpty() ? 1 : lessonsPerDay.size() + 1);
                lesson.setDate(date);
                lessonRepository.saveAndFlush(lesson);
            }
            date = date.plusDays(1);
        } while (date.getDayOfWeek().getValue() < DayOfWeek.SATURDAY.getValue());
    }
}
