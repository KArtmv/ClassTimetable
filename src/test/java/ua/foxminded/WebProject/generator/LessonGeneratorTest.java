package ua.foxminded.WebProject.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.service.*;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.Collections;
import java.util.random.RandomGenerator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LessonGeneratorTest {

    private final TestItems testItems = new TestItems();

    @Mock
    private LessonService lessonService;
    @Mock
    private CourseService courseService;
    @Mock
    private GroupService groupService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private ClassroomService classroomService;
    @Mock
    private RandomGenerator randomGenerator;
    @InjectMocks
    private LessonGenerator lessonGenerator;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fillLessonTable_shouldGenerateLessonDtoInstance_whenIsInvoke() {
        ReflectionTestUtils.setField(lessonGenerator, "maxLessons", 1);

        when(courseService.getAll()).thenReturn(testItems.getCourses());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(teacherService.getAll()).thenReturn(testItems.getTeachers());
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());
        when(lessonService.getAll()).thenReturn(Collections.emptyList()).thenReturn(Collections.singletonList(testItems.getLesson()));
        when(lessonService.saveLesson(any(LessonDto.class))).thenReturn(testItems.getLesson());
        when(randomGenerator.nextInt()).thenReturn(7).thenReturn(1).thenReturn(3).thenReturn(6);

        lessonGenerator.fillLessonTable();

        verify(courseService).getAll();
        verify(groupService).getAll();
        verify(teacherService).getAll();
        verify(classroomService).getAll();
        verify(lessonService).saveLesson(any(LessonDto.class));
    }

    @Test
    void fillLessonTable_shouldThrowException_whenDuplicateConstraints() {
        ReflectionTestUtils.setField(lessonGenerator, "maxLessons", 1);

        when(courseService.getAll()).thenReturn(testItems.getCourses());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(teacherService.getAll()).thenReturn(testItems.getTeachers());
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());
        when(lessonService.getAll()).thenReturn(Collections.emptyList()).thenReturn(Collections.singletonList(testItems.getLesson()));
        when(lessonService.saveLesson(any(LessonDto.class))).thenThrow(DataIntegrityViolationException.class);

        lessonGenerator.fillLessonTable();

        verify(courseService).getAll();
        verify(groupService).getAll();
        verify(teacherService).getAll();
        verify(classroomService).getAll();
    }
}