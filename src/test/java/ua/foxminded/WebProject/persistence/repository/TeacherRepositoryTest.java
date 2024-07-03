package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.persistence.entity.Staff;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class TeacherRepositoryTest {
    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Autowired
    private TeacherRepository repository;

    @Test
    void save_shouldReturnTeacherInstanceWithId_whenIsSavedSuccessfully() {
        assertThat(repository.save(testItems.getTeacher()).getId()).isNotNull();
    }

    @Test
    @Sql("/sql/teacher/teacher.sql")
    void findById_shouldReturnTeacherInstance_whenIsFounded() {
        Optional<Teacher> result = repository.findById(testData.getTeacherId());

        assertAll(() -> {
            assertTrue(result.isPresent());
            Teacher teacher = result.get();
            assertThat(teacher.getFirstName()).isEqualTo(testData.getTeacherFirstName());
            assertThat(teacher.getLastName()).isEqualTo(testData.getTeacherLastName());
        });
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenStudentIsNotFound() {
        assertFalse(repository.findById(testData.getUserId()).isPresent());
    }

    @Test
    @Sql("/sql/teacher/teachers.sql")
    void findAll_shouldReturnListOfTeachers_whenIsInvoke() {
        assertThat(repository.findAll()).hasSize(8);
    }

    @Test
    @Sql("/sql/teacher/teachers.sql")
    void delete_shouldDeleteStudent_whenIsInvoke() {
        assertAll(() -> {
            assertThat(repository.findAll()).hasSize(8);
            repository.delete(testItems.getFullyTeacher());
            assertThat(repository.findAll()).hasSize(7);
        });
    }

    @Test
    @Sql("/sql/teacher/teacher.sql")
    void getUserDataById_shouldReturnUserData_whenUserIsFound() {
        UserDto result = repository.getUserDataById(testData.getTeacherId());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.firstName()).isEqualTo(testData.getTeacherFirstName());
            assertThat(result.lastName()).isEqualTo(testData.getTeacherLastName());
            assertThat(result.email()).isEqualTo(testData.getTeacherEmail());
        });
    }
}