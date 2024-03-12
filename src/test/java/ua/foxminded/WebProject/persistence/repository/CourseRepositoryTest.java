package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.util.TestData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class CourseRepositoryTest {

    private static final TestData TEST_DATA = new TestData();

    @Autowired
    private CourseRepository repository;

    @Test
    @Sql("/sql/course/courses.sql")
    void findAll_shouldReturnListOfCourse_whenIsInvoke(){
        assertThat(repository.findAll()).hasSize(5);
    }

    @Test
    @Sql("/sql/course/course.sql")
    void findById(){
        Optional<Course> result = repository.findById(TEST_DATA.courseId);
        assertAll(() -> {
            assertTrue(result.isPresent());
            Course course = result.get();
            assertThat(course.getCourseName()).isEqualTo(TEST_DATA.courseName);
            assertThat(course.getCourseDescription()).isEqualTo(TEST_DATA.courseDescription);
        });
    }

    @Test
    void saveCourse_shouldReturnCourseInstanceWithId_whenIsSavedSuccessfully(){
        Course result = repository.save(TEST_DATA.getCourse());
        assertAll(() -> {
            assertThat(result.getId()).isNotNull();
            assertThat(result.getCourseName()).isEqualTo(TEST_DATA.courseName);
            assertThat(result.getCourseDescription()).isEqualTo(TEST_DATA.courseDescription);
        });
    }
}