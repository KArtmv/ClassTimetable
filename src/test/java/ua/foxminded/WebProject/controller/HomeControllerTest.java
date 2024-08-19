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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"student", "admin", "staff", "teacher"})
    void home_shouldReturnAppropriatePage_whenUserIsAuthenticated(String role) throws Exception {
        mockMvc.perform(get("/").with(testUser(role)))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/" + role));
    }

    @Test
    void home_shouldReturnAppropriatePage_whenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/").with(anonymous()))
                .andExpectAll(
                        status().isOk(),
                        view().name("home"));
    }
}