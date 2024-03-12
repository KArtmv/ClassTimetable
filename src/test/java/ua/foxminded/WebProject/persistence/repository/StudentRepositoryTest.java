package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.util.TestData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class StudentRepositoryTest {

        private final TestData testData = new TestData();
        @Autowired
        private StudentRepository repository;

        @Test
        @Sql(value = {"/sql/student/student.sql", "/sql/group/group.sql"})
        void findById_shouldReturnStudentInstance_whenIsFound(){
            Optional<Student> optionalResult = repository.findById(testData.studentId);
            assertAll(() ->  {
                assertTrue(optionalResult.isPresent());
                Student result = optionalResult.get();
                assertThat(result.getId()).isEqualTo(testData.studentId);
                assertThat(result.getFirstName()).isEqualTo(testData.studentFirstName);
                assertThat(result.getLastName()).isEqualTo(testData.studentLastName);
                assertThat(result.getGroup().getGroupName()).isEqualTo(testData.getGroup().getGroupName());
            });
        }

        @Test
        void findById_shouldReturnEmptyOptional_whenStudentIsNotFound(){
            assertFalse(repository.findById(testData.studentId).isPresent());
        }

        @Test
        @Sql("/sql/group/group.sql")
        void saveStudent_shouldReturnStudentInstanceWithId_whenSavedSuccessfully(){
            Student result = repository.save(testData.getStudent());
            assertAll(() -> {
                assertThat(result.getId()).isNotNull();
                assertThat(result.getFirstName()).isEqualTo(testData.studentFirstName);
                assertThat(result.getLastName()).isEqualTo(testData.studentLastName);
                assertThat(result.getGroup()).isEqualTo(testData.getGroup());
            });
        }

        @Test
        @Sql(scripts = {"/sql/student/student.sql", "/sql/group/group.sql"})
        void deleteStudent_shouldDoNothing_whenStudentDeleted(){
            Student student = new Student(testData.studentId);
            assertAll(() -> {
                assertThat(repository.findAll()).hasSize(1);
                repository.delete(student);
                assertThat(repository.findAll()).isEmpty();
            });
        }
    }

