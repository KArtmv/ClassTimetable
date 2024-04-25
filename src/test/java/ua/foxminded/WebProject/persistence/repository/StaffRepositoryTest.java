package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.WebProject.persistence.entity.Staff;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;

    @Test
    @Sql("/sql/staff/staffs.sql")
    void findAll_shouldReturnListOfAllStaffs_whenIsInvoke(){
        List<Staff> staffs = staffRepository.findAll();

        assertThat(staffs).hasSize(3);
    }
}