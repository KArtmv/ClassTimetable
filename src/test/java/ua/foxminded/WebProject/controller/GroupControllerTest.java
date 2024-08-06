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
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.StudentService;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;
import ua.foxminded.WebProject.util.GroupNameGenerator;
import ua.foxminded.WebProject.util.MyBeans;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class, MyBeans.class})
@WebMvcTest(GroupController.class)
class GroupControllerTest {

    private final TestItems testItems = new TestItems();
    private final TestData testData = new TestData();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;
    @MockBean
    private GroupNameGenerator groupNameGenerator;
    @MockBean
    private StudentService studentService;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void groupPage_shouldReturnGroupGage_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(groupService.getAll()).thenReturn(testItems.getGroups());

        mockMvc.perform(get("/group").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("groups"),
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

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void addGroup_shouldDisplayGroupForm_whenLoggedUserHasAccess(String role) throws Exception {
        when(groupNameGenerator.generateGroupName()).thenReturn(testData.getGroupName());
        mockMvc.perform(get("/group/addGroup").with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("group"),
                        view().name("group/features/addGroup"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void addGroup_shouldRespondWithForbidden_whenLoggedUserLacksAccess(String role) throws Exception {
        when(groupNameGenerator.generateGroupName()).thenReturn(testData.getGroupName());
        mockMvc.perform(get("/group/addGroup").with(testUser(role)))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void save_shouldRedirectToGroupPage_whenGroupSavedSuccessfully(String role) throws Exception {
        when(groupNameGenerator.generateGroupName()).thenReturn(testData.getGroupName());

        mockMvc.perform(post("/group/save")
                        .queryParam("groupName", testData.getGroupName())
                        .with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/group"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void save_shouldReturnGroupForm_whenGroupSavedFail(String role) throws Exception {
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(groupNameGenerator.generateGroupName()).thenReturn(testData.getGroupName());

        mockMvc.perform(post("/group/save")
                        .queryParam("groupName", testData.getGroupName())
                        .with(testUser(role)))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("group"),
                        view().name("group/features/addGroup"),
                        content().string(containsString("Group with given name: YS-27 already exists")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void save_shouldReturnForbidden_whenLoggedUserLacksAccess(String role) throws Exception {
        mockMvc.perform(post("/group/save").with(testUser(role)))
                .andDo(print())
                .andExpectAll(
                        status().is4xxClientError());
    }

    @Test
    void deleteGroupConfirmationForm() throws Exception {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());

        mockMvc.perform(get("/group/delete/{id}", testData.getGroupId()).with(testUser("admin")))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        view().name("group/features/deleteGroup"),
                        content().string(containsString("Are you sure you want to delete group:")),
                        content().string(containsString("YS-27")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher", "staff"})
    void deleteGroupConfirmationForm(String role) throws Exception {
        mockMvc.perform(get("/group/delete/{id}", testData.getGroupId()).with(testUser(role)))
                .andDo(print()).andExpect(
                        status().isForbidden());
    }

    @Test
    void remove() throws Exception {
        mockMvc.perform(post("/group/remove")
                        .queryParam("group", testData.getGroupId().toString())
                        .with(testUser("admin")))
                .andDo(print()).andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/group"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void studentsOfGroup_shouldReturnUserPage_whenUserRoleHasAccess(String role) throws Exception {
        when(studentService.getStudentsByGroupId(anyLong())).thenReturn(testItems.getStudentNames());

        mockMvc.perform(get("/group/students-of-group-{id}", testData.getGroupId()).with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("users"),
                        model().attributeExists("path"),
                        view().name("user/user"),
                        content().string(containsString(testData.getStaffFirstName())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void studentsOfGroup_shouldReturnStatusForbidden_whenUserRoleLackAccess(String role) throws Exception {
        mockMvc.perform(get("/group/students-of-group-{id}", testData.getGroupId()).with(testUser(role)))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void details_shouldReturnDetails_whenUserRoleHasAccess(String role) throws Exception {
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup());

        mockMvc.perform(get("/group/details-{id}", testData.getGroupId()).with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("group"),
                        view().name("group/features/details"),
                        content().string(containsString(testData.getGroupName())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void details_shouldReturnStatusForbidden_whenUserRoleLackAccess(String role) throws Exception {
        mockMvc.perform(get("/group/details-{id}", testData.getGroupId()).with(testUser(role)))
                .andDo(print()).andExpect(status().isForbidden());
    }




}