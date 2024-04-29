package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.TeacherRepository;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TeacherServiceImplTest {
    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private LessonRepository lessonRepository;
    @InjectMocks
    private TeacherServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTeacher_shouldReturnStudentInstanceWithId_whenIsSavedSuccessfully() {
        when(teacherRepository.save(any(Teacher.class))).thenReturn(testItems.getTeacherIdFirst());

        assertThat(service.saveTeacher(testItems.getTeacherDto()).getId()).isNotNull();
    }

    @Test
    void removeTeacherById_shouldRemoveTeacher_whenIsFound() {
        when(teacherRepository.findById(testItems.getTeacherId())).thenReturn(Optional.of(testItems.getTeacher()));

        service.removeTeacherById(new Teacher(testItems.getTeacherId()));

        verify(teacherRepository).findById(testItems.getTeacherId());
    }

    @Test
    void removeTeacherById_shouldThrowEntityNotFoundException_whenIsNotFound() {
        when(teacherRepository.findById(testItems.getTeacherId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.removeTeacherById(new Teacher(testItems.getTeacherId())));

        verify(teacherRepository).findById(testItems.getTeacherId());
    }

    @Test
    void getAll_shouldReturnListOfTeachers_whenIsInvoke() {
        when(teacherRepository.findAll()).thenReturn(testItems.getTeachers());

        assertThat(service.getAll()).hasSize(8);

        verify(teacherRepository).findAll();
    }

    @Test
    void getLessonForDay_shouldReturnListOfLessonsOfTeacher_whenIsInvoke() {
        when(lessonRepository.getLessonsByTeacherAndDate(any(Teacher.class), any(LocalDate.class))).thenReturn(testItems.getTeacherLessonsPerDay());

        assertThat(service.getLessonForDay(testItems.getTeacher(), testData.getDate())).hasSize(7);

        verify(lessonRepository).getLessonsByTeacherAndDate(testItems.getTeacher(), testData.getDate());
    }

    @Test
    void getLessonForWeek_shouldReturnListOfLessonsOfTeacher_whenIsInvoke() {
        when(lessonRepository.getLessonsByTeacherAndDateBetween(any(Teacher.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testItems.getTeacherLessonsPerWeek());

        assertThat(service.getLessonForWeek(testItems.getTeacher(), testItems.getDateOfMonday(), testItems.getDateOfFriday()))
                .hasSize(27);

        verify(lessonRepository).getLessonsByTeacherAndDateBetween(testItems.getTeacher(), testItems.getDateOfMonday(), testItems.getDateOfFriday());
    }

    @Test
    void getById_shouldReturnTeacherInstance_whenIdIsFound() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getTeacher()));

        assertThat(service.getById(testItems.getTeacherId())).isNotNull();

        verify(teacherRepository).findById(testItems.getTeacherId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(testItems.getTeacherId()));

        verify(teacherRepository).findById(testItems.getTeacherId());
    }
}