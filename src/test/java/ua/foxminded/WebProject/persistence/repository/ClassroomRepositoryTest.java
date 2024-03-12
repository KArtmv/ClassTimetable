package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.util.TestData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class ClassroomRepositoryTest {

    private final TestData testData = new TestData();
    @Autowired
    private ClassroomRepository repository;

    @Test
    @Sql("/sql/classroom/classrooms.sql")
    void findAll_shouldReturnListOfClassroom_whenIsInvoke(){
        assertThat(repository.findAll()).hasSize(5);
    }

    @Test
    @Sql("/sql/classroom/classroom.sql")
    void findById_shouldReturnClassroom_whenIsFound(){
        Optional<Classroom> result = repository.findById(testData.classroomId);
        assertAll(() -> {
            assertTrue(result.isPresent());
            Classroom classroom = result.get();
            assertThat(classroom.getClassroomName()).isEqualTo(testData.classroomName);
        });
    }
}