package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.service.ClassroomService;
import ua.foxminded.WebProject.service.CourseService;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.TeacherService;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LessonServiceImpl.class)
class LessonServiceImplTest {
    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @MockBean
    private LessonRepository lessonRepository;
    @MockBean
    private CourseService courseService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private ClassroomService classroomService;

    @Autowired
    private LessonServiceImpl lessonService;

    @Test
    void saveLesson_shouldReturnLessonInstanceWithId_whenIsSavedSuccessfully() {
        when(courseService.getById(anyLong())).thenReturn(testItems.getCourse());
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());
        when(classroomService.getById(anyLong())).thenReturn(testItems.getClassroom());
        when(teacherService.getById(anyLong())).thenReturn(testItems.getFullyTeacher());

        lessonService.saveLesson(testItems.getNewLesson());

        var argumentCaptor = ArgumentCaptor.forClass(Lesson.class);
        verify(groupService).getById(testData.getGroupId());
        verify(courseService).getById(testData.getCourseId());
        verify(classroomService).getById(testData.getClassroomId());
        verify(teacherService).getById(testData.getTeacherId());
        verify(lessonRepository).save(argumentCaptor.capture());

        assertAll(() -> {
            Lesson captureResult = argumentCaptor.getValue();
            assertThat(captureResult.getLessonNum()).isEqualTo(testData.getLessenNum());
            assertThat(captureResult.getCourse()).isEqualTo(testItems.getCourse());
            assertThat(captureResult.getGroup()).isEqualTo(testItems.getGroup());
            assertThat(captureResult.getClassroom()).isEqualTo(testItems.getClassroom());
            assertThat(captureResult.getTeacher()).isEqualTo(testItems.getFullyTeacher());
            assertThat(captureResult.getDate()).isEqualTo(testData.getDateOfMonday());
        });
    }

    @Test
    void saveLesson_shouldThrowEntityNotFoundException_whenGroupIdIsNotFound() {
        when(groupService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThat(lessonService.saveLesson(testItems.getNewLesson()).getId()).isNull();

        verify(groupService).getById(testData.getGroupId());
    }

    @Test
    void saveLesson_shouldThrowEntityNotFoundException_whenCourseIdIsNotFound() {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());
        when(courseService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThat(lessonService.saveLesson(testItems.getNewLesson()).getId()).isNull();

        verify(groupService).getById(testData.getGroupId());
        verify(courseService).getById(testData.getCourseId());
    }

    @Test
    void saveLesson_shouldThrowEntityNotFoundException_whenClassroomIdIsNotFound() {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());
        when(courseService.getById(anyLong())).thenReturn(testItems.getCourse());
        when(classroomService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThat(lessonService.saveLesson(testItems.getNewLesson()).getId()).isNull();

        verify(groupService).getById(testData.getGroupId());
        verify(courseService).getById(testData.getCourseId());
        verify(classroomService).getById(testData.getClassroomId());
    }

    @Test
    void saveLesson_shouldThrowEntityNotFoundException_whenTeacherIdIsNotFound() {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());
        when(courseService.getById(anyLong())).thenReturn(testItems.getCourse());
        when(classroomService.getById(anyLong())).thenReturn(testItems.getClassroom());
        when(teacherService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThat(lessonService.saveLesson(testItems.getNewLesson()).getId()).isNull();

        verify(groupService).getById(testData.getGroupId());
        verify(courseService).getById(testData.getCourseId());
        verify(classroomService).getById(testData.getClassroomId());
        verify(teacherService).getById(testData.getTeacherId());
    }

    @Test
    void getAll_shouldReturnListOfLesson_whenIsInvoke() {
        when(lessonRepository.findAll()).thenReturn(testItems.getAllLessons());

        assertThat(lessonService.getAll()).hasSize(206);

        verify(lessonRepository).findAll();
    }

    @Test
    void getAllLessonsPerDay_shouldReturnListOfLessonPerSpecifyDay_whenIsInvoke() {
        when(lessonRepository.getAllByDate(any(LocalDate.class))).thenReturn(testItems.lessonsByDate(testData.getDateOfMonday()));

        assertThat(lessonService.getLessonsPerDay(testData.getDateOfMonday())).hasSize(63);

        verify(lessonRepository).getAllByDate(testData.getDateOfMonday());
    }

    @Test
    void getById_shouldReturnLessonInstance_whenIsFound() {
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getLesson()));

        assertThat(lessonService.getById(testData.getLessonId()).getId()).isNotNull();

        verify(lessonRepository).findById(testData.getLessonId());
    }

    @Test
    void getAvailableNumberOfLessons_shouldReturnListOfFreeLessonsNums_whenIsInvoke() {
        when(lessonRepository.getLessonByGroupIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(testItems.groupLessonsPerDate(testData.getDateOfFriday()));

        assertThat(lessonService.getAvailableNumbersOfLessons(testData.getDateOfFriday(), testData.getGroupId())).hasSize(6);
    }

    @Test
    void getAvailableTeachers_shouldReturnListOfFreeTeachers_whenIsInvoke() {
        when(teacherService.getAllNames()).thenReturn(testItems.teacherNames());
        when(lessonRepository.getLessonsByDateAndLessonNum(any(LocalDate.class), anyInt())).thenReturn(testItems.getLessonsByDateAndLessonNum());

        assertThat(lessonService.getAvailableTeachers(testData.getDateOfThursday(), testData.getLessenNum())).hasSize(1);
    }


    @Test
    void getAvailableClassroom_shouldReturnListOfFreeTeachers_whenIsInvoke() {
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());
        when(lessonRepository.getLessonsByDateAndLessonNum(any(LocalDate.class), anyInt())).thenReturn(testItems.getLessonsByDateAndLessonNum());

        assertThat(lessonService.getAvailableClassrooms(testData.getDateOfMonday(), testData.getLessenNum())).hasSize(4);
    }

    @Test
    void getLessonDtoById_shouldReturnLessonDto_whenIsInvoke() {
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getLesson()));

        assertThat(lessonService.getLessonDtoById(testData.getLessonId())).isNotNull();
    }

    @Test
    void getLessonNums() {
        assertThat(lessonService.getLessonsNums()).hasSize(8);
    }

    @Test
    void update_shouldUpdateLesson_whenIsInvoke() {
        when(courseService.getById(anyLong())).thenReturn(testItems.getSecondCourse2());
        when(groupService.getById(anyLong())).thenReturn(testItems.getSecondGroup());
        when(classroomService.getById(anyLong())).thenReturn(testItems.getClassroom());
        when(teacherService.getById(anyLong())).thenReturn(testItems.getFullyTeacher());
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItems.getLesson()));


        lessonService.update(testItems.getLessonDtoUpdated());

        var argumentCaptor = ArgumentCaptor.forClass(Lesson.class);
        verify(groupService).getById(testData.getSecondGroupId());
        verify(courseService).getById(testData.getSecondCourseId());
        verify(classroomService).getById(testData.getClassroomId());
        verify(teacherService).getById(testData.getTeacherId());
        verify(lessonRepository).save(argumentCaptor.capture());

        assertAll(() -> {
            Lesson captureResult = argumentCaptor.getValue();
            assertThat(captureResult.getLessonNum()).isEqualTo(testData.getLessenNum());
            assertThat(captureResult.getCourse()).isEqualTo(testItems.getSecondCourse2());
            assertThat(captureResult.getGroup()).isEqualTo(testItems.getSecondGroup());
            assertThat(captureResult.getClassroom()).isEqualTo(testItems.getClassroom());
            assertThat(captureResult.getTeacher()).isEqualTo(testItems.getFullyTeacher());
            assertThat(captureResult.getDate()).isEqualTo(testData.getDateOfMonday());
        });
    }

    @Test
    void getLessonsByGroupIdPerDay() {
        when(lessonRepository.getLessonByGroupIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(testItems.groupLessonsPerDate(testData.getDateOfMonday()));

        assertThat(lessonService.getLessonsByGroupIdPerDay(testData.getGroupId(), testData.getDateOfMonday())).hasSize(8);

        verify(lessonRepository).getLessonByGroupIdAndDate(testData.getGroupId(), testData.getDateOfMonday());
    }

    @Test
    void getLessonsByGroupIdBetweenDates() {
        when(lessonRepository.getLessonsByGroupIdAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.getGroupLessons());

        assertThat(lessonService.getLessonsByGroupIdBetweenDates(testData.getGroupId(), testItems.getDateOfMonday(), testItems.getDateOfFriday())).hasSize(30);

        verify(lessonRepository).getLessonsByGroupIdAndDateBetween(testData.getGroupId(), testItems.getDateOfMonday(), testItems.getDateOfFriday());
    }

    @Test
    void getLessonsByTeacherIdPerDay() {
        when(lessonRepository.getLessonsByTeacherIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(testItems.teacherLessonsPerDate(testData.getDateOfMonday()));

        assertThat(lessonService.getLessonsByTeacherIdPerDay(testItems.getTeacherId(), testData.getDateOfMonday())).hasSize(8);

        verify(lessonRepository).getLessonsByTeacherIdAndDate(testItems.getTeacherId(), testData.getDateOfMonday());
    }

    @Test
    void getLessonsByTeacherIdBetweenDates() {
        when(lessonRepository.getLessonsByTeacherIdAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.getTeacherLessons());

        assertThat(lessonService.getLessonsByTeacherIdBetweenDates(testData.getTeacherId(), testItems.getDateOfMonday(), testItems.getDateOfFriday())).hasSize(26);

        verify(lessonRepository).getLessonsByTeacherIdAndDateBetween(testData.getTeacherId(), testItems.getDateOfMonday(), testItems.getDateOfFriday());
    }

    @Test
    void getLessonsBetweenDates() {
        when(lessonRepository.getLessonsByDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.lessonsBetweenDates(testData.getDateOfTuesday(), testData.getDateOfThursday()));

        assertThat(lessonService.getLessonsBetweenDates(testData.getDateOfTuesday(), testData.getDateOfThursday())).hasSize(135);

        verify(lessonRepository).getLessonsByDateBetween(testData.getDateOfTuesday(), testData.getDateOfThursday());
    }

    @Test
    void delete_shouldDoNothing_whenLessonIsDeleted() {
        lessonService.delete(testData.getLessonId());

        verify(lessonRepository).deleteById(testData.getLessonId());
    }
}