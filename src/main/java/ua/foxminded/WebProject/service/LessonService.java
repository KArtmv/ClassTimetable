package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.ClassroomDto;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.DTO.NamesDto;
import ua.foxminded.WebProject.persistence.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface LessonService extends AbstractService<Lesson, Long> {
    Lesson saveLesson(LessonDto lesson);

    List<Student> getParticipantsOfLesson(Lesson lesson);

    List<Lesson> getAllLessonsPerDay(LocalDate date);

    boolean isTableEmpty();

    List<Integer> getAllLessonsNumberByGroupAndDate(LocalDate date, Long groupId);

    List<NamesDto> getAvailableTeachers(LocalDate date, Integer lessonNum);

    List<ClassroomDto> getAvailableClassrooms(LocalDate date, Integer lessonNum);
}
