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
import ua.foxminded.WebProject.service.StudentService;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    private final TestItems testItems = new TestItems();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "student", "staff"})
    void studentPage_shouldReturnStudentPage_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(service.getAll()).thenReturn(testItems.getStudents());

        this.mockMvc.perform(get("/student").with(testUser(role))).andDo(print())
                .andExpectAll(
                        content().string(containsString("Liam")),
                        content().string(containsString("Jones")),
                        content().string(containsString("YS-27")),
                        content().string(containsString("Harper")),
                        content().string(containsString("Robinson")),
                        content().string(containsString("YS-27")),
                        content().string(containsString("Lucas")),
                        content().string(containsString("Thompson")),
                        content().string(containsString("YS-27")),
                        content().string(containsString("Carter")),
                        content().string(containsString("Clark")),
                        content().string(containsString("YS-27")),
                        content().string(containsString("Amelia")),
                        content().string(containsString("Martinez")),
                        content().string(containsString("YS-27")));
    }

    @Test
    void studentPage_shouldReturnForbiddenStatus_whenLoginUserWithAbleAccess() throws Exception {
        this.mockMvc.perform(get("/student").with(testUser("teacher")))
                .andExpect(status().isForbidden());
    }
}