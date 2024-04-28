package ua.foxminded.WebProject.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.service.TeacherService;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    private final TestItems testItems = new TestItems();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "teacher", "staff"})
    void teacherPage_shouldReturnTeacherPage_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(teacherService.getAll()).thenReturn(testItems.getTeachers());

        mockMvc.perform(get("/teacher").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("teachers"),
                        model().attribute("teachers", testItems.getTeachers()),
                        view().name("teacher/teacher"),
                        content().string(containsString("John")),
                        content().string(containsString("Smith")),
                        content().string(containsString("Emily")),
                        content().string(containsString("Johnson")),
                        content().string(containsString("Michael")),
                        content().string(containsString("Davis")),
                        content().string(containsString("Sarah")),
                        content().string(containsString("Wilson")),
                        content().string(containsString("Robert")),
                        content().string(containsString("Taylor")),
                        content().string(containsString("Jessica")),
                        content().string(containsString("Brown")),
                        content().string(containsString("William")),
                        content().string(containsString("Moore")),
                        content().string(containsString("Olivia")),
                        content().string(containsString("Anderson")));
    }

    @Test
    void teacherPage_shouldReturnForbiddenStatus_whenLoginUserWithoutAbleAccess() throws Exception {
        this.mockMvc.perform(get("/teacher").with(testUser("student")))
                .andExpect(status().isForbidden());
    }
}