package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.util.TestData;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
@Sql(value = "/sql/user/users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {

    private final TestData testData = new TestData();

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserByEmail_shouldReturnUserWithGivenEmail_whenIsExist() {
        Optional<User> result = userRepository.findUserByEmail(testData.getAdminEmail());

        assertAll(() -> {
                assertTrue(result.isPresent());
                User user = result.get();
                assertEquals(user.getFirstName(), testData.getAdminFirstName());
                assertEquals(user.getLastName(), testData.getAdminLastName());
        });
    }

    @Test
    void findUserByEmail_shouldReturnEmptyOptional_whenIsNotExist() {
        assertTrue(userRepository.findUserByEmail(testData.getEmail()).isEmpty());
    }
}