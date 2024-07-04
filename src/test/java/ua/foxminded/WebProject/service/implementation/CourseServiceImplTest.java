package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.repository.CourseRepository;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CourseServiceImpl.class)
class CourseServiceImplTest {
    private final TestItems testItems = new TestItems();
    private final TestData testData = new TestData();

    @MockBean
    private CourseRepository repository;
    @Autowired
    private CourseServiceImpl service;

    @Test
    void findAll_shouldReturnListOfCourses_whenIsInvoke() {
        when(repository.findAll()).thenReturn(testItems.getCourses());

        assertThat(service.getAll()).hasSize(3);

        verify(repository).findAll();
    }

    @Test
    void saveCourse_shouldReturnCourseInstanceWithId_whenIsSavedSuccessfully() {
        when(repository.save(any(Course.class))).thenReturn(testItems.getCourse());

        Course result = service.saveCourse(testData.getCourse());

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

    @Test
    void update_shouldReturnUpdatingCourseInstance_whenIsUpdatedSuccessfully() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getCourse()));
        when(repository.save(any(Course.class))).thenReturn(testItems.getSecondCourse());

        assertAll(() -> {
            Course result = service.update(testItems.getSecondCourse());
            assertThat(result.getId()).isEqualTo(testData.getCourseId());
            assertThat(result.getCourseName()).isEqualTo(testData.getSecondCourseName());
            assertThat(result.getCourseDescription()).isEqualTo(testData.getSecondCourseDescription());
        });

        verify(repository).findById(testData.getCourseId());
        verify(repository).save(any(Course.class));
    }

    @Test
    void remove_shouldDoNothing_whenIsRemovedSuccessfully() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getCourse()));

        service.remove(testItems.getCourse());

        verify(repository).findById(testData.getCourseId());
        verify(repository).delete(testItems.getCourse());
    }
}