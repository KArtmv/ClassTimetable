package ua.foxminded.WebProject.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.service.CourseService;
import ua.foxminded.WebProject.testDataInstance.TestItems;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(CourseController.class)
class CourseControllerTest {

    private final TestItems testItems = new TestItems();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void coursePage_shouldReturnCoursePage_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(courseService.getAll()).thenReturn(testItems.getCourses());

        mockMvc.perform(get("/course").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("courses"),
                        model().attribute("courses", courseService.getAll()),
                        view().name("course/course"),
                        content().string(containsString("Principles of Economics")),
                        content().string(containsString("Learn about the fundamentals of economics")),
                        content().string(containsString("World History: Ancient Civilizations")),
                        content().string(containsString("Explore the history of ancient civilizations")),
                        content().string(containsString("Creative Writing Workshop")),
                        content().string(containsString("Develop your writing skills in a creative environment")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void coursePage_shouldReturnForbiddenStatus_whenLoginUserWithoutAbleAccess(String role) throws Exception {
        mockMvc.perform(get("/course").with(testUser(role)))
                .andExpect(status().isForbidden());
    }
}