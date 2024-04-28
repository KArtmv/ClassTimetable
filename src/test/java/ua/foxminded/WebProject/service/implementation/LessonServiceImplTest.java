package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.service.ClassroomService;
import ua.foxminded.WebProject.service.CourseService;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.TeacherService;
import ua.foxminded.WebProject.util.MyLocalDate;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LessonServiceImplTest {
    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Mock
    private LessonRepository repository;
    @Mock
    private CourseService courseService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private GroupService groupService;
    @Mock
    private ClassroomService classroomService;
    @Mock
    private MyLocalDate localDate;
    @InjectMocks
    private LessonServiceImpl lessonService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("ua.foxminded.WebProject.util.DataOfParametrizedTest#getLessons")
    void saveLesson_shouldReturnLessonInstanceWithId_whenIsSavedSuccessfully(List<Lesson> lessonsPerWeek, List<Lesson> lessonsPerDay) {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());
        when(courseService.getById(anyLong())).thenReturn(testItems.getCourseWithIdSeven());
        when(classroomService.getById(anyLong())).thenReturn(testItems.getClassroom());
        when(teacherService.getById(anyLong())).thenReturn(testItems.getTeacher());
        when(localDate.getCurrentDate()).thenReturn(testData.getDate());

        when(repository.findByGroupAndDateBetween(any(Group.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(lessonsPerWeek).thenReturn(Collections.emptyList());
        when(repository.getLessonByGroupAndDate(any(Group.class), any(LocalDate.class))).thenReturn(lessonsPerDay).thenReturn(Collections.emptyList());
        when(repository.saveAndFlush(any(Lesson.class))).thenReturn(testItems.getLesson());

        assertThat(lessonService.saveLesson(testItems.getLessonDto()).getId()).isNotNull();

        verify(groupService).getById(testData.getGroupId());
        verify(courseService).getById(testItems.getCourseId());
        verify(classroomService).getById(testItems.getClassroomId());
        verify(teacherService).getById(testItems.getTeacherId());
    }

    @Test
    void saveLesson_shouldThrowEntityNotFoundException_whenGroupIdIsNotFound() {
        when(groupService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThat(lessonService.saveLesson(testItems.getLessonDto()).getId()).isNull();

        verify(groupService).getById(testData.getGroupId());
    }

    @Test
    void saveLesson_shouldThrowEntityNotFoundException_whenCourseIdIsNotFound() {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());
        when(courseService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThat(lessonService.saveLesson(testItems.getLessonDto()).getId()).isNull();

        verify(groupService).getById(testData.getGroupId());
        verify(courseService).getById(testItems.getCourseId());
    }

    @Test
    void saveLesson_shouldThrowEntityNotFoundException_whenClassroomIdIsNotFound() {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());
        when(courseService.getById(anyLong())).thenReturn(testItems.getCourseWithIdSeven());
        when(classroomService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThat(lessonService.saveLesson(testItems.getLessonDto()).getId()).isNull();

        verify(groupService).getById(testData.getGroupId());
        verify(courseService).getById(testItems.getCourseId());
        verify(classroomService).getById(testItems.getClassroomId());
    }

    @Test
    void saveLesson_shouldThrowEntityNotFoundException_whenTeacherIdIsNotFound() {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());
        when(courseService.getById(anyLong())).thenReturn(testItems.getCourseWithIdSeven());
        when(classroomService.getById(anyLong())).thenReturn(testItems.getClassroom());
        when(teacherService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThat(lessonService.saveLesson(testItems.getLessonDto()).getId()).isNull();

        verify(groupService).getById(testData.getGroupId());
        verify(courseService).getById(testItems.getCourseId());
        verify(classroomService).getById(testItems.getClassroomId());
        verify(teacherService).getById(testItems.getTeacherId());
    }

    @Test
    void getAll_shouldReturnListOfLesson_whenIsInvoke() {
        when(repository.findAll()).thenReturn(testItems.getAllLessons());

        assertThat(lessonService.getAll()).hasSize(206);

        verify(repository).findAll();
    }

    @Test
    void getAllLessonsPerDay_shouldReturnListOfLessonPerSpecifyDay_whenIsInvoke() {
        when(repository.getAllByDate(any(LocalDate.class))).thenReturn(testItems.getLessonsPerDay());

        assertThat(lessonService.getAllLessonsPerDay(testData.getDate())).hasSize(63);

        verify(repository).getAllByDate(testData.getDate());
    }

    @Test
    void getById_shouldReturnLessonInstance_whenIsFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getLesson()));

        assertThat(lessonService.getById(testData.getLessonId())).isNotNull();

        verify(repository).findById(testData.getLessonId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lessonService.getById(testData.getLessonId()));

        verify(repository).findById(testData.getLessonId());
    }

    @Test
    void getParticipantsOfLesson_shouldReturnListOfStudents_whenLessonIsFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getParticipants()));

        assertThat(lessonService.getParticipantsOfLesson(new Lesson(testData.getLessonId()))).hasSize(5);

        verify(repository).findById(anyLong());
    }

    @Test
    void getParticipantsOfLesson_shouldThrowEntityNotFoundException_whenLessonIsNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(lessonService.getParticipantsOfLesson(new Lesson(testData.getLessonId()))).isEmpty();

        verify(repository).findById(anyLong());
    }
}