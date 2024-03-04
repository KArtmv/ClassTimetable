package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.persistence.entity.Lesson;

import java.util.List;

public interface LessonService extends AbstractService<Lesson, Long> {
    Lesson saveLesson(LessonDto lesson);

    List<Lesson> getAll();
}
