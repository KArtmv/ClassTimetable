package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.repository.CourseRepository;
import ua.foxminded.WebProject.util.TestData;
import ua.foxminded.WebProject.util.TestItems;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CourseServiceImplTest {
    private final TestItems testItems = new TestItems();
    private final TestData testData = new TestData();
    @Mock
    private CourseRepository repository;
    @InjectMocks
    private CourseServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnListOfCourses_whenIsInvoke() {
        when(repository.findAll()).thenReturn(testItems.getCourses());

        assertThat(service.getAll()).hasSize(3);

        verify(repository).findAll();
    }

    @Test
    void saveCourse_shouldReturnCourseInstanceWithId_whenIsSavedSuccessfully() {
        when(repository.save(any(Course.class))).thenReturn(testItems.getCourse());

        Course result = service.saveCourse(testData.getCourseDto());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.getCourseName()).isEqualTo(testData.getCourseName());
            assertThat(result.getCourseDescription()).isEqualTo(testData.getCourseDescription());
        });
    }

    @Test
    void getById_shouldReturnCourseInstance_whenIsFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getCourse()));

        Course result = service.getById(testData.getCourseId());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.getCourseName()).isEqualTo(testData.getCourseName());
            assertThat(result.getCourseDescription()).isEqualTo(testData.getCourseDescription());
        });
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(testData.getCourseId()));

        verify(repository).findById(testData.getCourseId());
    }
}