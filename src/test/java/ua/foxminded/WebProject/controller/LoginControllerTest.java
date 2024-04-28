package ua.foxminded.WebProject.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(controllers = LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "teacher", "student"})
    void login_shouldReturnForbidden_whenUserIsAuthorized(String role) throws Exception {
        this.mockMvc.perform(get("/login").with(testUser(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void login_shouldReturnLoginPage_whenUserIsNotAuthorized() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpectAll(
                        status().isOk(),
                        view().name("login"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "teacher", "student"})
    void loginError_shouldReturnForbidden_whenUserIsAuthorized(String role) throws Exception {
        this.mockMvc.perform(get("/login-error").with(testUser(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void loginError_shouldReturnLoginPage_whenUserIsNotAuthorized() throws Exception {
        this.mockMvc.perform(get("/login-error")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("loginError"),
                        model().attribute("loginError", true),
                        view().name("login"),
                        content().string(containsString("**Login attempt unsuccessful.** Please double-check your username/email and password.")));
    }

}