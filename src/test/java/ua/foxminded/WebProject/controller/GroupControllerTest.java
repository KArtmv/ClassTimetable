package ua.foxminded.WebProject.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(GroupController.class)
class GroupControllerTest {

    private final TestItems testItems = new TestItems();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void groupPage_shouldReturnGroupGage_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(groupService.getAll()).thenReturn(testItems.getGroups());

        mockMvc.perform(get("/group").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("groups"),
                        model().attribute("groups", groupService.getAll()),
                        view().name("group/group"),
                        content().string(containsString("YS-27")),
                        content().string(containsString("HV-14")),
                        content().string(containsString("QM-09")));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"student", "teacher"})
    void groupPage_shouldForbiddenStatus_whenLoginUserWithoutAbleAccess(String role) throws Exception {
        when(groupService.getAll()).thenReturn(testItems.getGroups());

        mockMvc.perform(get("/group").with(testUser(role)))
                .andExpect(status().isForbidden());
    }
}