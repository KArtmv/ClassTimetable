package ua.foxminded.WebProject.util;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class DataOfParametrizedTest {
    private static final TestItems testItems = new TestItems();

    public static Stream<Arguments> getLessons() {
        return Stream.of(
                Arguments.of(testItems.getAllLessonsOfGroupPerWeek(), testItems.getAllLessonsOfGroupPerDay()),
                Arguments.of(testItems.getAllLessonsOfGroupPerWeekWithoutCourse(), testItems.getAllLessonsOfGroupPerDayWithoutCourse())
        );
    }

}
