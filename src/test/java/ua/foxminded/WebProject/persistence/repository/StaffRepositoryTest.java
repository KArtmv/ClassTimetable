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
import ua.foxminded.WebProject.persistence.entity.Staff;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class StaffRepositoryTest {

    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Autowired
    private StaffRepository staffRepository;

    @Test
    @Sql("/sql/staff/staff.sql")
    void findById_shouldReturnStudentInstance_whenIsFound() {
        Optional<Staff> optionalResult = staffRepository.findById(testData.getStaffId());

        assertAll(() -> {
            assertTrue(optionalResult.isPresent());
            Staff result = optionalResult.get();
            assertThat(result.getId()).isEqualTo(testData.getStaffId());
            assertThat(result.getFirstName()).isEqualTo(testData.getStaffFirstName());
            assertThat(result.getLastName()).isEqualTo(testData.getStaffLastName());
            assertThat(result.getEmail()).isEqualTo(testData.getStaffEmail());
        });
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenStudentIsNotFound() {
        assertFalse(staffRepository.findById(testData.getUserId()).isPresent());
    }

    @Test
    void save_shouldReturnStudentInstanceWithId_whenSavedSuccessfully() {
        assertThat(staffRepository.save(testItems.getStaff()).getId()).isNotNull();
    }

    @Test
    @Sql("/sql/staff/staff.sql")
    void delete_shouldDoNothing_whenStudentDeleted() {
        assertAll(() -> {
            assertThat(staffRepository.findAll()).hasSize(1);
            staffRepository.delete(testItems.getFullyStaff());
            assertThat(staffRepository.findAll()).isEmpty();
        });
    }

    @Test
    @Sql("/sql/staff/staffs.sql")
    void findAll_shouldReturnAllStudents_whenAllStudentsAreFound() {
        assertThat(staffRepository.findAll()).hasSize(3);
    }

    @Test
    @Sql("/sql/staff/staff.sql")
    void getUserDataById_shouldReturnUserData_whenUserIsFound() {
        UserDto result = staffRepository.getUserDataById(testData.getStaffId());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.firstName()).isEqualTo(testData.getStaffFirstName());
            assertThat(result.lastName()).isEqualTo(testData.getStaffLastName());
            assertThat(result.email()).isEqualTo(testData.getStaffEmail());
        });
    }
}