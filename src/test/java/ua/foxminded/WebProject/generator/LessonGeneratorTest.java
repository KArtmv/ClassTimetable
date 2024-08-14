package ua.foxminded.WebProject.generator;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.service.*;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;
import ua.foxminded.WebProject.util.MyLocalDate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.random.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = LessonGenerator.class)
class LessonGeneratorTest {

    private final TestItems testItems = new TestItems();
    private final TestData testData = new TestData();

    @MockBean
    private LessonService lessonService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private ClassroomService classroomService;
    @MockBean
    private RandomGenerator randomGenerator;
    @MockBean
    private MyLocalDate localDate;
    @Autowired
    private LessonGenerator lessonGenerator;

    @Test
    void fillLessonTable_shouldGenerateLessonDtoInstance_whenIsInvoke() {
        ReflectionTestUtils.setField(lessonGenerator, "LESSON_COUNT", 1);

        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfTuesday());
        when(courseService.getAll()).thenReturn(testItems.getCourses());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(teacherService.getAll()).thenReturn(testItems.getTeachers());
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());

        when(lessonService.getAll()).thenReturn(Collections.emptyList()).thenReturn(Collections.singletonList(testItems.getLesson()));

        when(randomGenerator.nextLong(anyLong(), anyLong())).thenReturn(testData.getCourseId()).thenReturn(testData.getTeacherId()).thenReturn(testData.getClassroomId()).thenReturn(testData.getGroupId());

        when(lessonService.getLessonsByGroupIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(Collections.emptyList());

        lessonGenerator.fillLessonTable();

        ArgumentCaptor<LessonDto> capture = ArgumentCaptor.forClass(LessonDto.class);
        verify(courseService).getAll();
        verify(groupService).getAll();
        verify(teacherService).getAll();
        verify(classroomService).getAll();
        verify(lessonService).saveLesson(capture.capture());
        LessonDto lessonDto = capture.getValue();
        assertAll(() -> {
            assertThat(lessonDto.getLessonNum()).isEqualTo(testData.getLessenNum());
            assertThat(lessonDto.getDate()).isEqualTo(testData.getDateOfMonday());
            assertThat(lessonDto.getCourseId()).isEqualTo(testData.getCourseId());
            assertThat(lessonDto.getTeacherId()).isEqualTo(testData.getTeacherId());
            assertThat(lessonDto.getClassroomId()).isEqualTo(testData.getClassroomId());
            assertThat(lessonDto.getGroupId()).isEqualTo(testData.getGroupId());
        });
    }

    @Test
    void fillLessonTable_shouldGenerateLessonDtoInstance_whenDateIsWeekend() {
        ReflectionTestUtils.setField(lessonGenerator, "LESSON_COUNT", 1);

        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfTuesday().with(DayOfWeek.SATURDAY));
        when(courseService.getAll()).thenReturn(testItems.getCourses());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(teacherService.getAll()).thenReturn(testItems.getTeachers());
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());

        when(lessonService.getAll()).thenReturn(Collections.emptyList()).thenReturn(Collections.singletonList(testItems.getLesson()));

        when(randomGenerator.nextLong(anyLong(), anyLong())).thenReturn(testData.getCourseId()).thenReturn(testData.getTeacherId()).thenReturn(testData.getClassroomId()).thenReturn(testData.getGroupId());

        when(lessonService.getLessonsByGroupIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(Collections.emptyList());
        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(Collections.emptyList());

        lessonGenerator.fillLessonTable();

        ArgumentCaptor<LessonDto> capture = ArgumentCaptor.forClass(LessonDto.class);
        verify(courseService).getAll();
        verify(groupService).getAll();
        verify(teacherService).getAll();
        verify(classroomService).getAll();
        verify(lessonService).saveLesson(capture.capture());
        LessonDto lessonDto = capture.getValue();
        assertAll(() -> {
            assertThat(lessonDto.getLessonNum()).isEqualTo(testData.getLessenNum());
            assertThat(lessonDto.getDate()).isEqualTo(testData.getDateOfMonday().plusWeeks(1));
            assertThat(lessonDto.getCourseId()).isEqualTo(testData.getCourseId());
            assertThat(lessonDto.getTeacherId()).isEqualTo(testData.getTeacherId());
            assertThat(lessonDto.getClassroomId()).isEqualTo(testData.getClassroomId());
            assertThat(lessonDto.getGroupId()).isEqualTo(testData.getGroupId());
        });
    }

    @Test
    void fillLessonTable_shouldSaveLessonToNextWeek_whenCourseCountMoreThenAvailable() {
        ReflectionTestUtils.setField(lessonGenerator, "LESSON_COUNT", 1);

        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfTuesday());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(teacherService.getAll()).thenReturn(testItems.getTeachers());
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());

        when(lessonService.getAll()).thenReturn(Collections.emptyList()).thenReturn(Collections.singletonList(testItems.getLesson()));

        when(randomGenerator.nextLong(anyLong(), anyLong())).thenReturn(testData.getCourseId()).thenReturn(testData.getTeacherId()).thenReturn(testData.getClassroomId()).thenReturn(testData.getGroupId());

        when(lessonService.getLessonsByGroupIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.groupLessonsBetweenDates(testData.getDateOfMonday(), testData.getDateOfFriday())).thenReturn(Collections.emptyList());
        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(Collections.emptyList());

        lessonGenerator.fillLessonTable();

        ArgumentCaptor<LessonDto> capture = ArgumentCaptor.forClass(LessonDto.class);
        verify(courseService).getAll();
        verify(groupService).getAll();
        verify(teacherService).getAll();
        verify(classroomService).getAll();
        verify(lessonService).saveLesson(capture.capture());
        LessonDto lessonDto = capture.getValue();
        assertAll(() -> {
            assertThat(lessonDto.getLessonNum()).isEqualTo(testData.getLessenNum());
            assertThat(lessonDto.getDate()).isEqualTo(testData.getDateOfMonday().plusWeeks(1));
            assertThat(lessonDto.getCourseId()).isEqualTo(testData.getCourseId());
            assertThat(lessonDto.getTeacherId()).isEqualTo(testData.getTeacherId());
            assertThat(lessonDto.getClassroomId()).isEqualTo(testData.getClassroomId());
            assertThat(lessonDto.getGroupId()).isEqualTo(testData.getGroupId());
        });

    }

    @Test
    void fillLessonTable_shouldSaveLessonToNextWeekToTuesday_whenLessonsPerMondayMoreThanAvailable() {
        ReflectionTestUtils.setField(lessonGenerator, "LESSON_COUNT", 1);

        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfTuesday());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(teacherService.getAll()).thenReturn(testItems.getTeachers());
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());

        when(lessonService.getAll()).thenReturn(Collections.emptyList()).thenReturn(Collections.singletonList(testItems.getLesson()));

        when(randomGenerator.nextLong(anyLong(), anyLong())).thenReturn(testData.getCourseId()).thenReturn(testData.getTeacherId()).thenReturn(testData.getClassroomId()).thenReturn(testData.getGroupId());

        when(lessonService.getLessonsByGroupIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.groupLessonsBetweenDates(testData.getDateOfMonday(), testData.getDateOfFriday())).thenReturn(Collections.emptyList());
        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(testItems.groupLessonsPerDate(testData.getDateOfMonday())).thenReturn(Collections.emptyList());

        lessonGenerator.fillLessonTable();

        ArgumentCaptor<LessonDto> capture = ArgumentCaptor.forClass(LessonDto.class);
        verify(courseService).getAll();
        verify(groupService).getAll();
        verify(teacherService).getAll();
        verify(classroomService).getAll();
        verify(lessonService).saveLesson(capture.capture());
        LessonDto lessonDto = capture.getValue();
        assertAll(() -> {
            assertThat(lessonDto.getLessonNum()).isEqualTo(testData.getLessenNum());
            assertThat(lessonDto.getDate()).isEqualTo(testData.getDateOfTuesday().plusWeeks(1).with(DayOfWeek.TUESDAY));
            assertThat(lessonDto.getCourseId()).isEqualTo(testData.getCourseId());
            assertThat(lessonDto.getTeacherId()).isEqualTo(testData.getTeacherId());
            assertThat(lessonDto.getClassroomId()).isEqualTo(testData.getClassroomId());
            assertThat(lessonDto.getGroupId()).isEqualTo(testData.getGroupId());
        });
    }

    @Test
    void fillLessonTable_shouldSaveLesson_whenLessonsPerDayLesserThanMax() {
        ReflectionTestUtils.setField(lessonGenerator, "LESSON_COUNT", 1);

        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfTuesday());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(teacherService.getAll()).thenReturn(testItems.getTeachers());
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());

        when(lessonService.getAll()).thenReturn(Collections.emptyList()).thenReturn(Collections.singletonList(testItems.getLesson()));

        when(randomGenerator.nextLong(anyLong(), anyLong())).thenReturn(testData.getCourseId()).thenReturn(testData.getTeacherId()).thenReturn(testData.getClassroomId()).thenReturn(testData.getGroupId());

        when(lessonService.getLessonsByGroupIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.getAllLessonsOfGroupPerWeekWithoutCourse());
        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(testItems.groupLessonsPerDate(testData.getDateOfMonday())).thenReturn(testItems.groupLessonWithCourse()).thenReturn(testItems.getAllLessonsOfGroupPerDayWithoutCourse());

        lessonGenerator.fillLessonTable();

        ArgumentCaptor<LessonDto> capture = ArgumentCaptor.forClass(LessonDto.class);
        verify(courseService).getAll();
        verify(groupService).getAll();
        verify(teacherService).getAll();
        verify(classroomService).getAll();
        verify(lessonService).saveLesson(capture.capture());
        LessonDto lessonDto = capture.getValue();
        assertAll(() -> {
            assertThat(lessonDto.getLessonNum()).isEqualTo(8);
            assertThat(lessonDto.getDate()).isEqualTo(testData.getDateOfWednesday());
            assertThat(lessonDto.getCourseId()).isEqualTo(testData.getCourseId());
            assertThat(lessonDto.getTeacherId()).isEqualTo(testData.getTeacherId());
            assertThat(lessonDto.getClassroomId()).isEqualTo(testData.getClassroomId());
            assertThat(lessonDto.getGroupId()).isEqualTo(testData.getGroupId());
        });
    }
}