package ua.foxminded.WebProject.util;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.repository.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(InitialDataLoader.class)
@ActiveProfiles("test")
@FlywayTest
class InitialDataLoaderTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private InitialDataLoader dataLoader;

    @Test
    @Transactional
    void populate_ShouldReturnTrue_WhenScriptExecutesSuccessfully() {
        assertTrue(studentRepository.findAll().isEmpty());
        assertTrue(teacherRepository.findAll().isEmpty());
        assertTrue(courseRepository.findAll().isEmpty());
        assertTrue(groupRepository.findAll().isEmpty());
        assertTrue(staffRepository.findAll().isEmpty());
        assertTrue(classroomRepository.findAll().isEmpty());

        dataLoader.populate();

        assertFalse(studentRepository.findAll().isEmpty());
        assertFalse(teacherRepository.findAll().isEmpty());
        assertFalse(courseRepository.findAll().isEmpty());
        assertFalse(groupRepository.findAll().isEmpty());
        assertFalse(staffRepository.findAll().isEmpty());
        assertFalse(classroomRepository.findAll().isEmpty());
    }
}