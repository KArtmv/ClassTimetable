package ua.foxminded.WebProject.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.WebProject.persistence.repository.StaffRepository;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(controllers = StaffController.class)
class StaffControllerTest {

    private final TestItems testItems = new TestItems();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StaffRepository staffRepository;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void staffPage_shouldReturnStaffPage_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(staffRepository.findAll()).thenReturn(testItems.getStaffs());

        mockMvc.perform(get("/staff").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("staffs"),
                        model().attribute("staffs", staffRepository.findAll()),
                        view().name("staff/staff"));

    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void staffPage_shouldReturnForbiddenStatus_whenLoginUserWithoutAbleAccess(String role) throws Exception {
        this.mockMvc.perform(get("/staff").with(testUser(role)))
                .andExpect(status().isForbidden());
    }
}