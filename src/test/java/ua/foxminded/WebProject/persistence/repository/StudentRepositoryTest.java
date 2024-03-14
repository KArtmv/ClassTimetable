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
            Optional<Student> optionalResult = repository.findById(testData.getStudentId());
            assertAll(() ->  {
                assertTrue(optionalResult.isPresent());
                Student result = optionalResult.get();
                assertThat(result.getId()).isEqualTo(testData.getStudentId());
                assertThat(result.getFirstName()).isEqualTo(testData.getStudentFirstName());
                assertThat(result.getLastName()).isEqualTo(testData.getStudentLastName());
                assertThat(result.getGroup().getGroupName()).isEqualTo(testData.getGroup().getGroupName());
            });
        }

        @Test
        void findById_shouldReturnEmptyOptional_whenStudentIsNotFound(){
            assertFalse(repository.findById(testData.getStudentId()).isPresent());
        }

        @Test
        @Sql("/sql/group/group.sql")
        void saveStudent_shouldReturnStudentInstanceWithId_whenSavedSuccessfully(){
                assertThat(repository.save(testData.getStudent()).getId()).isNotNull();
        }

        @Test
        @Sql(scripts = {"/sql/student/student.sql", "/sql/group/group.sql"})
        void deleteStudent_shouldDoNothing_whenStudentDeleted(){
            Student student = new Student(testData.getStudentId());
            assertAll(() -> {
                assertThat(repository.findAll()).hasSize(1);
                repository.delete(student);
                assertThat(repository.findAll()).isEmpty();
            });
        }
    }

