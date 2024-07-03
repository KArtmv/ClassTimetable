package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.persistence.entity.Admin;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class AdminRepositoryTest {

    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Autowired
    private AdminRepository adminRepository;

    @Test
    @Sql(scripts = {"/sql/admin/admin.sql"})
    void findById_shouldReturnStudentInstance_whenIsFound() {
        Optional<Admin> optionalResult = adminRepository.findById(testData.getAdminId());

        assertAll(() -> {
            assertTrue(optionalResult.isPresent());
            Admin result = optionalResult.get();
            assertThat(result.getId()).isEqualTo(testData.getAdminId());
            assertThat(result.getFirstName()).isEqualTo(testData.getAdminFirstName());
            assertThat(result.getLastName()).isEqualTo(testData.getAdminLastName());
            assertThat(result.getEmail()).isEqualTo(testData.getAdminEmail());
        });
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenStudentIsNotFound() {
        assertFalse(adminRepository.findById(testData.getUserId()).isPresent());
    }

    @Test
    void save_shouldReturnStudentInstanceWithId_whenSavedSuccessfully() {
        assertThat(adminRepository.save(testItems.getAdmin()).getId()).isNotNull();
    }

    @Test
    @Sql(scripts = {"/sql/admin/admin.sql"})
    void delete_shouldDoNothing_whenStudentDeleted() {
        assertAll(() -> {
            assertThat(adminRepository.findAll()).hasSize(1);
            adminRepository.delete(testItems.getFullyAdmin());
            assertThat(adminRepository.findAll()).isEmpty();
        });
    }

    @Test
    @Sql("/sql/admin/admins.sql")
    void findAll_shouldReturnAllStudents_whenAllStudentsAreFound() {
        assertThat(adminRepository.findAll()).hasSize(3);
    }

    @Test
    @Sql(scripts = {"/sql/admin/admin.sql"})
    void getUserDataById_shouldReturnUserData_whenUserIsFound() {
        UserDto result = adminRepository.getUserDataById(testData.getAdminId());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.firstName()).isEqualTo(testData.getAdminFirstName());
            assertThat(result.lastName()).isEqualTo(testData.getAdminLastName());
            assertThat(result.email()).isEqualTo(testData.getAdminEmail());
        });
    }
}

