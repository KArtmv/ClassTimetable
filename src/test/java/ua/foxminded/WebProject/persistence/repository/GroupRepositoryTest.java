package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.util.TestData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class GroupRepositoryTest {

    private final TestData testData = new TestData();
    @Autowired
    private GroupRepository repository;

    @Test
    @Sql("/sql/group/groups.sql")
    void findAll_shouldReturnListOfGroup_whenIsInvoke() {
        assertThat(repository.findAll()).hasSize(4);
    }

    @Test
    @Sql("/sql/group/group.sql")
    void findById_shouldReturnGroupInstance_whenIsFound() {
        Optional<Group> result = repository.findById(testData.getGroupId());
        assertAll(() -> {
            assertTrue(result.isPresent());
            Group group = result.get();
            assertThat(group.getGroupName()).isEqualTo(testData.getGroupName());
        });
    }

    @Test
    void save_shouldReturnGroupInstanceWithId_whenIsSavedSuccessfully() {
        Group result = repository.save(testData.getGroup());
        assertThat(result.getId()).isNotNull();
    }
}