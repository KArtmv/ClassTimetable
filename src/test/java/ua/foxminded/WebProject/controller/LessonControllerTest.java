package ua.foxminded.WebProject.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.WebProject.persistence.entity.Lesson;
import ua.foxminded.WebProject.service.LessonService;
import ua.foxminded.WebProject.util.TestItems;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@WebMvcTest(LessonController.class)
class LessonControllerTest {

    private final TestItems testItems = new TestItems();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Test
    void lessonPage() throws Exception {
        when(lessonService.getAll()).thenReturn(testItems.getLessonsPerDay());

        mockMvc.perform(get("/lesson")).andDo(print())
            .andExpectAll(
                status().isOk(),
                model().attributeExists("lessons"),
                model().attribute("lessons", lessonService.getAll()));
    }
}