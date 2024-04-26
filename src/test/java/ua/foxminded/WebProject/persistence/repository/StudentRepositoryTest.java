package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.util.TestData;
import ua.foxminded.WebProject.util.TestItems;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql("/sql/group/group.sql")
class StudentRepositoryTest {

    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Autowired
    private StudentRepository studentRepository;

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(value = {"/sql/student/student.sql"})
    void findById_shouldReturnStudentInstance_whenIsFound() {
        Optional<Student> optionalResult = studentRepository.findById(testData.getStudentId());
        assertAll(() -> {
            assertTrue(optionalResult.isPresent());
            Student result = optionalResult.get();
            assertThat(result.getId()).isEqualTo(testData.getStudentId());
            assertThat(result.getFirstName()).isEqualTo(testData.getStudentFirstName());
            assertThat(result.getLastName()).isEqualTo(testData.getStudentLastName());
            assertThat(result.getGroup().getGroupName()).isEqualTo(testData.getGroup().getGroupName());
        });
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenStudentIsNotFound() {
        assertFalse(studentRepository.findById(testData.getStudentId()).isPresent());
    }

    @Test
    void saveStudent_shouldReturnStudentInstanceWithId_whenSavedSuccessfully() {
        assertThat(studentRepository.save(testItems.getFullStudent()).getId()).isNotNull();
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(scripts = {"/sql/student/student.sql"})
    void deleteStudent_shouldDoNothing_whenStudentDeleted() {
        Student student = new Student(testData.getStudentId());
        assertAll(() -> {
            assertThat(studentRepository.findAll()).hasSize(1);
            studentRepository.delete(student);
            assertThat(studentRepository.findAll()).isEmpty();
        });
    }

    @Test
    @Sql(scripts = {"/sql/group/groups.sql", "/sql/student/students.sql"})
    void findAll_shouldReturnAllStudents_whenAllStudentsAreFound() {
        assertThat(studentRepository.findAll()).hasSize(3);
    }
}

