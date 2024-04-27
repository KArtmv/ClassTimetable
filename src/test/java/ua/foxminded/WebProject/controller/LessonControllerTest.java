package ua.foxminded.WebProject.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.service.LessonService;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;
import ua.foxminded.WebProject.util.TestItems;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.foxminded.WebProject.util.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(LessonController.class)
class LessonControllerTest {

    private final TestItems testItems = new TestItems();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void lessonPage_shouldReturnLessonPage_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(lessonService.getAll()).thenReturn(testItems.getLessonsPerDay());

        mockMvc.perform(get("/lesson").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("lessons"),
                        model().attribute("lessons", lessonService.getAll()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void lessonPage_shouldReturnLessonPage_whenLoginUserWithoutAbleAccess(String role) throws Exception {
        mockMvc.perform(get("/lesson").with(testUser(role)))
                .andExpect(status().isForbidden());
    }
}