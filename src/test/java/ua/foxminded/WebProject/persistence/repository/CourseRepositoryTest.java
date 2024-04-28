package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.testDataInstance.TestData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class CourseRepositoryTest {

    private final TestData testData = new TestData();

    @Autowired
    private CourseRepository repository;

    @Test
    @Sql("/sql/course/courses.sql")
    void findAll_shouldReturnListOfCourse_whenIsInvoke() {
        assertThat(repository.findAll()).hasSize(5);
    }

    @Test
    @Sql("/sql/course/course.sql")
    void findById() {
        Optional<Course> result = repository.findById(testData.getCourseId());
        assertAll(() -> {
            assertTrue(result.isPresent());
            Course course = result.get();
            assertThat(course.getCourseName()).isEqualTo(testData.getCourseName());
            assertThat(course.getCourseDescription()).isEqualTo(testData.getCourseDescription());
        });
    }

    @Test
    void saveCourse_shouldReturnCourseInstanceWithId_whenIsSavedSuccessfully() {
        Course result = repository.save(testData.getCourse());
        assertAll(() -> {
            assertThat(result.getId()).isNotNull();
            assertThat(result.getCourseName()).isEqualTo(testData.getCourseName());
            assertThat(result.getCourseDescription()).isEqualTo(testData.getCourseDescription());
        });
    }
}