package ua.foxminded.WebProject.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.persistence.entity.Lesson;
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
    private static int LESSON_COUNT = 350;

    private final MyLocalDate localDate;
    private final GroupService groupService;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final LessonService lessonService;
    private final RandomGenerator randomGenerator;


    public void fillLessonTable() {
        int groupsSize = groupService.getAll().size();
        int coursesSize = courseService.getAll().size();
        int teachersSize = teacherService.getAll().size();
        int classroomsSize = classroomService.getAll().size();

        while (lessonService.getAll().size() < LESSON_COUNT) {
            assignLessonToAvailableWeek(createLessonDto(coursesSize, groupsSize, teachersSize, classroomsSize));
        }
    }

    private LessonDto createLessonDto(Integer coursesSize, Integer teachersSize, Integer classroomsSize, Integer groupsSize) {
        LessonDto lessonDto = new LessonDto();
        lessonDto.setCourseId(generateRandomId(coursesSize));
        lessonDto.setTeacherId(generateRandomId(teachersSize));
        lessonDto.setClassroomId(generateRandomId(classroomsSize));
        lessonDto.setGroupId(generateRandomId(groupsSize));

        return lessonDto;
    }

    private long generateRandomId(Integer size) {
        return randomGenerator.nextLong(1L, size + 1L);
    }

    private void assignLessonToAvailableWeek(LessonDto lessonDto) {
        LocalDate mondayDate = getMondayDate();
        int attempts = 0;
        int maxAttempts = 3;

        do {
            if (isCourseCountLess(lessonDto, mondayDate) < MAX_AMOUNT_LESSONS_BY_COURSE_PER_WEEK) {
                assignLessonToAvailableDay(mondayDate, lessonDto);
                break;
            }
            mondayDate = mondayDate.plusWeeks(1);
            attempts++;
        } while (attempts < maxAttempts);
    }

    private LocalDate getMondayDate() {
        return isWeekend(localDate.getCurrentDate()) ? localDate.getCurrentDate().plusWeeks(1).with(DayOfWeek.MONDAY) : localDate.getCurrentDate().with(DayOfWeek.MONDAY);
    }

    private boolean isWeekend(LocalDate date) {
        return date.isEqual(date.with(DayOfWeek.SATURDAY)) || date.isEqual(date.with(DayOfWeek.SUNDAY));
    }

    private long isCourseCountLess(LessonDto lessonDto, LocalDate mondayDate) {
        return lessonService.getLessonsByGroupIdBetweenDates(lessonDto.getGroupId(), mondayDate, mondayDate.with(DayOfWeek.FRIDAY)).stream()
                .filter(l -> l.getCourse().getId().equals(lessonDto.getCourseId())).count();
    }

    private void assignLessonToAvailableDay(LocalDate date, LessonDto lessonDto) {
        do {
            List<Lesson> lessonsPerDay = lessonService.getLessonsByGroupIdPerDay(lessonDto.getGroupId(), date);

            if (lessonsPerDay.size() < MAX_AMOUNT_LESSONS_PER_DAY && isHasCourse(lessonDto, lessonsPerDay)) {
                lessonDto.setLessonNum(lessonsPerDay.isEmpty() ? 1 : lessonsPerDay.size() + 1);
                lessonDto.setDate(date);
                lessonService.saveLesson(lessonDto);
                break;
            }
            date = date.plusDays(1);
        } while (date.getDayOfWeek().getValue() < DayOfWeek.SATURDAY.getValue());
    }

    private static boolean isHasCourse(LessonDto lessonDto, List<Lesson> lessonsPerDay) {
        return lessonsPerDay.stream().noneMatch(l -> l.getCourse().getId().equals(lessonDto.getCourseId()));
    }
}
