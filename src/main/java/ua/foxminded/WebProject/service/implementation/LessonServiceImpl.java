package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.DTO.NamesDto;
import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.service.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LessonServiceImpl implements LessonService {

    private static final List<Integer> LESSONS_NUMS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

    private final LessonRepository lessonRepository;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final ClassroomService classroomService;

    @Override
    @Transactional(rollbackFor = DataIntegrityViolationException.class)
    public Lesson saveLesson(LessonDto lessonDto) {
        try {
            Lesson lesson = verifyId(lessonDto, new Lesson());
            lesson.setLessonNum(lessonDto.getLessonNum());
            lesson.setDate(lessonDto.getDate());
            return lessonRepository.save(lesson);
        } catch (EntityNotFoundException e) {
            log.error("Failed adding lesson: {}", e.getMessage());
            return new Lesson();
        }
    }

    @Override
    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson getById(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found given id:" + id));
    }

    @Override
    public List<Integer> getAvailableNumbersOfLessons(LocalDate date, Long groupId) {
        return LESSONS_NUMS.stream()
                .filter(integer -> getLessonsByGroupIdPerDay(groupId, date)
                        .stream().map(Lesson::getLessonNum).noneMatch(integer::equals)).toList();
    }

    @Override
    public List<NamesDto> getAvailableTeachers(LocalDate date, Integer lessonNum) {
        return teacherService.getAllNames().stream().filter(t -> lessonRepository.getLessonsByDateAndLessonNum(date, lessonNum)
                .stream().map(l -> l.getTeacher().getId()).noneMatch(t.id()::equals)).toList();
    }

    @Override
    public List<Classroom> getAvailableClassrooms(LocalDate date, Integer lessonNum) {
        return classroomService.getAll().stream().filter(c -> lessonRepository.getLessonsByDateAndLessonNum(date, lessonNum)
                .stream().map(Lesson::getClassroom).noneMatch(c::equals)).toList();
    }

    @Override
    public LessonDto getLessonDtoById(Long id) {
        Lesson lesson = getById(id);
        return new LessonDto(lesson.getId(),
                lesson.getDate(),
                lesson.getLessonNum(),
                lesson.getCourse().getId(),
                lesson.getGroup().getId(),
                lesson.getClassroom().getId(),
                lesson.getTeacher().getId());
    }

    @Override
    public List<Integer> getLessonsNums() {
        return LESSONS_NUMS;
    }

    @Override
    @Transactional
    public void update(LessonDto lessonDto) {
        Lesson lesson = verifyId(lessonDto, getById(lessonDto.getId()));
        lesson.setDate(lessonDto.getDate());
        lesson.setLessonNum(lessonDto.getLessonNum());
        lessonRepository.save(lesson);
    }

    @Override
    public List<Lesson> getLessonsByGroupIdPerDay(Long groupId, LocalDate date) {
        return lessonRepository.getLessonByGroupIdAndDate(groupId, date);
    }

    @Override
    public List<Lesson> getLessonsByGroupIdBetweenDates(Long groupId, LocalDate start, LocalDate end) {
        return lessonRepository.getLessonsByGroupIdAndDateBetween(groupId, start, end);
    }

    @Override
    public List<Lesson> getLessonsByTeacherIdPerDay(Long teacherId, LocalDate localDate) {
        return lessonRepository.getLessonsByTeacherIdAndDate(teacherId, localDate);
    }

    @Override
    public List<Lesson> getLessonsByTeacherIdBetweenDates(Long teacherId, LocalDate start, LocalDate end) {
        return lessonRepository.getLessonsByTeacherIdAndDateBetween(teacherId, start, end);
    }

    @Override
    public List<Lesson> getLessonsPerDay(LocalDate date) {
        return lessonRepository.getAllByDate(date);
    }

    @Override
    public List<Lesson> getLessonsBetweenDates(LocalDate start, LocalDate end) {
        return lessonRepository.getLessonsByDateBetween(start, end);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        lessonRepository.deleteById(id);
    }

    private Lesson verifyId(LessonDto lessonDto, Lesson lesson) {
        lesson.setGroup(groupService.getById(lessonDto.getGroupId()));
        lesson.setCourse(courseService.getById(lessonDto.getCourseId()));
        lesson.setClassroom(classroomService.getById(lessonDto.getClassroomId()));
        lesson.setTeacher(teacherService.getById(lessonDto.getTeacherId()));
        return lesson;
    }
}
