package ua.foxminded.WebProject.persistence.repository;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.util.TestData;
import ua.foxminded.WebProject.util.TestItems;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@FlywayTest
class LessonRepositoryTest {

    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Autowired
    private LessonRepository repository;

    @Test
    @Sql("/sql/lesson/lessons.sql")
    void findAll(){
        assertThat(repository.findAll()).hasSize(206);
    }

    @Test
    @Sql("/sql/lesson/lessons.sql")
    void getAllLessonsPerDay_shouldReturnListOfLessons_whenIsInvoke(){
        assertThat(repository.getAllByDate(testData.getDate())).hasSize(63);
    }

    @Test
    @Sql("/sql/lesson/lesson.sql")
    void getById_shouldReturnLessonInstance_whenIsFound(){
        Optional<Lesson> result = repository.findById(testData.lessonId);
        assertAll(() -> {
            assertTrue(result.isPresent());
            Lesson lesson = result.get();
            assertThat(lesson.getLessonNum()).isEqualTo(testData.lessenNum);
            assertThat(lesson.getDate()).isEqualTo(testData.getDate());
            assertThat(lesson.getCourse().getId()).isEqualTo(testItems.getCourse().getId());
            assertThat(lesson.getGroup().getId()).isEqualTo(testItems.getGroup().getId());
            assertThat(lesson.getClassroom().getId()).isEqualTo(testItems.getClassroom().getId());
            assertThat(lesson.getTeacher().getId()).isEqualTo(testItems.getTeacher().getId());
        });
    }

    @Test
    @Sql(scripts = {"/sql/lesson/lessons.sql"})
    void getLessonByGroupAndDate_shouldReturnListOfLesson_whenIsInvoke() {
        assertThat(repository.getLessonByGroupAndDate(testItems.getGroup(), testData.getDate())).hasSize(8);
    }

    @Test
    @Sql("/sql/lesson/lessons.sql")
    void findByGroupAndDateBetween() {
        assertThat(repository.findByGroupAndDateBetween(
                testItems.getGroup(),
                testData.getDate().with(DayOfWeek.TUESDAY),
                testData.getDate().with(DayOfWeek.THURSDAY))).hasSize(20);
    }

    @Test
    @Sql("/sql/lesson/lessons.sql")
    void getLessonsByTeacherAndDate() {
        assertThat(repository.getLessonsByTeacherAndDate(testItems.getTeacher(), testData.getDate())).hasSize(7);
    }

    @Test
    @Sql("/sql/lesson/lessons.sql")
    void getLessonsByTeacherAndDateBetween() {
        assertThat(repository.getLessonsByTeacherAndDateBetween(
                testItems.getTeacher(),
                testData.getDate().with(DayOfWeek.WEDNESDAY),
                testData.getDate().with(DayOfWeek.FRIDAY))).hasSize(13);
    }

    @Test
    void save_shouldReturnLessonInstanceWithId_whenIsSavedSuccessfully(){
        Lesson result = repository.save(testData.getLesson());
        assertThat(result.getId()).isNotNull();
    }
}