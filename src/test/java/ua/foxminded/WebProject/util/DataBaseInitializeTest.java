package ua.foxminded.WebProject.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.ApplicationArguments;
import ua.foxminded.WebProject.generator.LessonGenerator;
import ua.foxminded.WebProject.service.LessonService;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.Collections;

import static org.mockito.Mockito.*;

class DataBaseInitializeTest {
    private static final TestItems TEST_ITEMS = new TestItems();

    @Mock
    private InitialDataLoader dataLoader;

    @Mock
    private LessonGenerator lessonGenerator;

    @Mock
    private LessonService lessonService;

    @Mock
    private ApplicationArguments applicationArguments;

    @InjectMocks
    private DataBaseInitialize dataBaseInitialize;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_InitializeDatabaseSuccessfully() {
        when(lessonService.getAll()).thenReturn(Collections.emptyList());

        dataBaseInitialize.run(applicationArguments);

        verify(lessonService).getAll();
        verify(dataLoader).populate();
        verify(lessonGenerator).fillLessonTable();
    }

    @Test
    void run_DatabaseNotInitialized() {
        when(lessonService.getAll()).thenReturn(TEST_ITEMS.getAllLessons());

        dataBaseInitialize.run(applicationArguments);

        verify(lessonService).getAll();
        verify(dataLoader, never()).populate();
        verify(lessonGenerator, never()).fillLessonTable();
    }

    @Test
    void run_ScriptExecutionFails() {
        when(lessonService.getAll()).thenReturn(Collections.emptyList());
        doThrow(RuntimeException.class).when(dataLoader).populate();

        dataBaseInitialize.run(applicationArguments);

        verify(lessonService).getAll();
        verify(dataLoader).populate();
        verify(lessonGenerator, never()).fillLessonTable();
    }
}