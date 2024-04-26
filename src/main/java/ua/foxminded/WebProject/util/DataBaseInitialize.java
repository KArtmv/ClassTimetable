package ua.foxminded.WebProject.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ua.foxminded.WebProject.generator.LessonGenerator;
import ua.foxminded.WebProject.service.LessonService;

@Component
@RequiredArgsConstructor
@Profile("!test")
@Slf4j
public class DataBaseInitialize implements ApplicationRunner {

    private final SqlScriptExecutor scriptExecutor;
    private final LessonGenerator lessonGenerator;
    private final LessonService lessonService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            if (lessonService.isTableEmpty() && scriptExecutor.execute()) {
                lessonGenerator.fillLessonTable();
            }
        } catch (Exception e) {
            log.error("Failed to initialize database: {}", e.getMessage());
        }
    }
}
