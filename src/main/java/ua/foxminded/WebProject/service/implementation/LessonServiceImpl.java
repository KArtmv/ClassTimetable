package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.ClassroomDto;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.DTO.NamesDto;
import ua.foxminded.WebProject.persistence.entity.*;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.service.*;
import ua.foxminded.WebProject.util.MyLocalDate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LessonServiceImpl implements LessonService {

    private static final List<Integer> LESSONS_COUNT = Arrays.asList(1,2,3,4,5,6,7,8);

    private final LessonRepository lessonRepository;
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final ClassroomService classroomService;

    @Override
    @Transactional(rollbackFor = DataIntegrityViolationException.class)
    public Lesson saveLesson(LessonDto lessonDto) {
        Lesson lesson = new Lesson();
        try {
            lesson = verifyId(lessonDto);
            lesson.setLessonNum(lessonDto.getLessonNum());
            lesson.setDate(lessonDto.getDate());
            lessonRepository.save(lesson);
        } catch (EntityNotFoundException e) {
            log.error("Failed adding lesson: {}", e.getMessage());
        }
        return lesson;
    }

    @Override
    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    @Override
    public List<Lesson> getAllLessonsPerDay(LocalDate date) {
        return lessonRepository.getAllByDate(date);
    }

    @Override
    public boolean isTableEmpty() {
        return lessonRepository.count() == 0;
    }

    @Override
    public List<Integer> getAllLessonsNumberByGroupAndDate(LocalDate date, Long groupId) {
        return LESSONS_COUNT.stream()
                .filter(integer -> lessonRepository.getLessonByGroupAndDate(groupService.getById(groupId), date)
                        .stream().map(Lesson::getLessonNum).noneMatch(integer::equals)).toList();
    }

    @Override
    public Lesson getById(Long id) {
        return lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found given id:" + id));
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

    @Override
    public List<NamesDto> getAvailableTeachers(LocalDate date, Integer lessonNum) {
        return teacherService.getAllNames().stream().filter(t -> lessonRepository.getLessonsByDateAndLessonNum(date, lessonNum)
                .stream().map(l -> l.getTeacher().getId()).noneMatch(t.id()::equals)).toList();
    }

    @Override
    public List<ClassroomDto> getAvailableClassrooms(LocalDate date, Integer lessonNum) {
        return classroomService.getAllClassrooms().stream().filter(c -> lessonRepository.getLessonsByDateAndLessonNum(date, lessonNum)
                .stream().map(l -> l.getClassroom().getId()).noneMatch(c.id()::equals)).toList();
    }

    private Lesson verifyId(LessonDto lessonDto) {
        Lesson lesson = new Lesson();
        lesson.setGroup(groupService.getById(lessonDto.getGroupId()));
        lesson.setCourse(courseService.getById(lessonDto.getCourseId()));
        lesson.setClassroom(classroomService.getById(lessonDto.getClassroomId()));
        lesson.setTeacher(teacherService.getById(lessonDto.getTeacherId()));
        return lesson;
    }
}
