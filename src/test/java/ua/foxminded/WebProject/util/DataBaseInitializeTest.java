package ua.foxminded.WebProject.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.ApplicationArguments;
import ua.foxminded.WebProject.generator.LessonGenerator;
import ua.foxminded.WebProject.service.LessonService;

import static org.mockito.Mockito.*;

class DataBaseInitializeTest {

    @Mock
    private SqlScriptExecutor scriptExecutor;

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
        when(lessonService.isTableEmpty()).thenReturn(true);
        when(scriptExecutor.execute()).thenReturn(true);

        dataBaseInitialize.run(applicationArguments);

        verify(lessonService).isTableEmpty();
        verify(scriptExecutor).execute();
        verify(lessonGenerator).fillLessonTable();
    }

    @Test
    void run_DatabaseNotInitialized() {
        when(lessonService.isTableEmpty()).thenReturn(false);

        dataBaseInitialize.run(applicationArguments);

        verify(lessonService).isTableEmpty();
        verify(scriptExecutor, never()).execute();
        verify(lessonGenerator, never()).fillLessonTable();
    }

    @Test
    void run_ScriptExecutionFails() {
        when(lessonService.isTableEmpty()).thenReturn(true);
        when(scriptExecutor.execute()).thenReturn(false);

        dataBaseInitialize.run(applicationArguments);

        verify(lessonService).isTableEmpty();
        verify(scriptExecutor).execute();
        verify(lessonGenerator, never()).fillLessonTable();
    }


    @Test
    void run_ScriptExecutionFails_whenThrowsException() {
        when(lessonService.isTableEmpty()).thenReturn(true);
        when(scriptExecutor.execute()).thenThrow(RuntimeException.class);

        dataBaseInitialize.run(applicationArguments);

        verify(lessonService).isTableEmpty();
        verify(scriptExecutor).execute();
        verify(lessonGenerator, never()).fillLessonTable();
    }
}