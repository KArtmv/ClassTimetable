package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(scripts = {"/sql/group/group.sql", "/sql/student/student.sql"})
class StudentRepositoryTest {

    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Autowired
    private StudentRepository studentRepository;

    @Test
    void findById_shouldReturnStudentInstance_whenIsFound() {
        Optional<Student> optionalResult = studentRepository.findById(testData.getStudentId());
        assertAll(() -> {
            assertTrue(optionalResult.isPresent());
            Student result = optionalResult.get();
            assertThat(result.getId()).isEqualTo(testData.getStudentId());
            assertThat(result.getFirstName()).isEqualTo(testData.getStudentFirstName());
            assertThat(result.getLastName()).isEqualTo(testData.getStudentLastName());
            assertThat(result.getGroup().getGroupName()).isEqualTo(testData.getGroupName());
        });
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenStudentIsNotFound() {
        assertFalse(studentRepository.findById(testData.getUserId()).isPresent());
    }

    @Test
    void saveStudent_shouldReturnStudentInstanceWithId_whenSavedSuccessfully() {
        assertThat(studentRepository.save(testItems.getFullyStudent()).getId()).isNotNull();
    }

    @Test
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

    @Test
    void getUserDataById_shouldReturnUserData_whenUserIsFound() {
        UserDto result = studentRepository.getUserDataById(testData.getStudentId());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.firstName()).isEqualTo(testData.getStudentFirstName());
            assertThat(result.lastName()).isEqualTo(testData.getStudentLastName());
            assertThat(result.email()).isEqualTo(testData.getStudentEmail());
        });
    }

    @Test
    void getStudentGroup_shouldReturnStudentGroup_whenStudentIsFound() {
        StudentGroupDto result = studentRepository.getStudentGroup(testData.getStudentId());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.groupId()).isEqualTo(testData.getGroupId());
            assertThat(result.groupName()).isEqualTo(testData.getGroupName());
        });
    }
}

