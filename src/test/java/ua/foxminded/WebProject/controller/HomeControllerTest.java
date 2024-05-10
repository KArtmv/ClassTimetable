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
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;
import ua.foxminded.WebProject.util.MyLocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(HomeController.class)
class HomeControllerTest {

    private final TestData testData = new TestData();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MyLocalDate localDate;

    @ParameterizedTest
    @ValueSource(strings = {"student", "admin", "staff", "teacher"})
    void home_shouldReturnAppropriatePage_whenUserIsAuthenticated(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDate());

        mockMvc.perform(get("/").with(testUser(role)))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/" + role));
    }

    @Test
    void home_shouldReturnAppropriatePage_whenUserIsNotAuthenticated() throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDate());

        mockMvc.perform(get("/").with(anonymous()))
                .andExpectAll(
                        status().isOk(),
                        view().name("home"),
                        content().string(containsString(testData.getDate().toString())));
    }
}