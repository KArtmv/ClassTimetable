package ua.foxminded.WebProject.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void adminPage() throws Exception {
        mockMvc.perform(get("/admin").with(testUser("admin")))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        view().name("admin/admin"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"staff", "teacher", "student"})
    void adminPage_shouldReturnForbidden_whenUserLacksAccess(String role) throws Exception {
        mockMvc.perform(get("/admin").with(testUser(role)))
                .andDo(print()).andExpect(status().isForbidden());
    }
}