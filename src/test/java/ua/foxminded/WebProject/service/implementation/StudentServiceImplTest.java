package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.StudentRepository;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.util.TestData;
import ua.foxminded.WebProject.util.TestItems;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {
    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Mock
    private GroupService groupService;
    @Mock
    private StudentRepository repository;
    @Mock
    private LessonRepository lessonRepository;
    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnStudentInstance_whenIsFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getStudent()));

        assertThat(studentService.getById(testData.getStudentId())).isNotNull();

        verify(repository).findById(testData.getStudentId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound(){
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> studentService.getById(testData.getStudentId()));

        verify(repository).findById(testData.getStudentId());
    }

    @Test
    void saveStudent_shouldReturnStudentInstanceWithId_whenIsSavedSuccessfully() {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());
        when(repository.save(any(Student.class))).thenReturn(testItems.getStudent());

        assertThat(studentService.saveStudent(testItems.getStudentDto()).getId()).isNotNull();

        verify(groupService).getById(testData.getGroupId());
    }

    @Test
    void saveStudent_shouldReturnStudentInstanceWithoutId_whenGroupIdIsNotFound(){
        when(groupService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThat(studentService.saveStudent(testItems.getStudentDto()).getId()).isNull();

        verify(groupService).getById(testData.getGroupId());
    }

    @Test
    void removeStudentById_shouldRemoveStudent_whenIsFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getStudent()));
        studentService.removeStudentById(new Student(testData.getStudentId()));

        verify(repository).delete(testItems.getStudent());
    }

    @Test
    void removeStudentById_shouldThrowEntityNotFoundException_whenStudentIsNotFound(){
        when(repository.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> studentService.removeStudentById(new Student(testData.getStudentId())));

        verify(repository).findById(testData.getStudentId());
    }

    @Test
    void getLessonsForDay_shouldReturnListOfLessonsPerSpecifyDay_whenIsInvoke() {
        when(lessonRepository.getLessonByGroupAndDate(any(Group.class), any(LocalDate.class))).thenReturn(testItems.getAllLessonsOfGroupPerDay());

        assertThat(studentService.getLessonsForDay(testItems.getStudent(), testItems.getDateOfMonday())).hasSize(8);

        verify(lessonRepository).getLessonByGroupAndDate(testItems.getGroup(), testItems.getDateOfMonday());
    }

    @Test
    void getLessonsForWeek_shouldReturnListOfLessonsPerSpecifyWeek_whenIsInvoke() {
        when(lessonRepository.findByGroupAndDateBetween(any(Group.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.getAllLessonsOfGroupPerWeek());

        assertThat(studentService.getLessonsForWeek(testItems.getStudent(), testItems.getDateOfMonday(), testItems.getDateOfFriday())).hasSize(30);

        verify(lessonRepository).findByGroupAndDateBetween(testItems.getGroup(), testItems.getDateOfMonday(), testItems.getDateOfFriday());
    }
}