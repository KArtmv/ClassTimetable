package ua.foxminded.WebProject.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ua.foxminded.WebProject.form.UserRegistrationForm;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.StudentService;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    private final TestItems testItems = new TestItems();
    private final TestData testData = new TestData();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "student", "staff"})
    void userPage_shouldReturnPageWithListOfUser_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(studentService.getAll()).thenReturn(testItems.getStudents());

        this.mockMvc.perform(get("/student").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/user"),
                        model().attributeExists("users"),
                        content().string(containsString("Liam")),
                        content().string(containsString("Jones")),
                        content().string(containsString("Harper")),
                        content().string(containsString("Robinson")),
                        content().string(containsString("Lucas")),
                        content().string(containsString("Thompson")),
                        content().string(containsString("Carter")),
                        content().string(containsString("Clark")),
                        content().string(containsString("Amelia")),
                        content().string(containsString("Martinez")));
    }

    @Test
    void userPage_shouldReturnForbiddenStatus_whenLoginUserWithoutAbleAccess() throws Exception {
        this.mockMvc.perform(get("/student").with(testUser("teacher")))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void addUser_shouldReturnAddUserView_whenLoggedUserHasAccess(String role) throws Exception {
        when(groupService.getAll()).thenReturn(testItems.getGroups());

        mockMvc.perform(get("/student/addUser").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("registrationForm"),
                        model().attributeExists("user"),
                        model().attributeExists("path"),
                        model().attributeExists("groups"),
                        model().attributeDoesNotExist("emptyGroup"),
                        view().name("user/addUser"),
                        content().string(containsString("YS-27")),
                        content().string(containsString("HV-14")),
                        content().string(containsString("QM-09")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "student"})
    void addUser_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        when(groupService.getAll()).thenReturn(testItems.getGroups());

        mockMvc.perform(get("/student/addUser").with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void saveUser_shouldRedirectToUserPage_whenReceivedDataIsValid(String role) throws Exception {
        when(studentService.saveUser(any(UserRegistrationForm.class))).thenReturn((Student) testItems.getSavedStudent());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", testData.getStudentFirstName());
        multiValueMap.add("lastName", testData.getStudentLastName());
        multiValueMap.add("email", testData.getEmail());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());
        multiValueMap.add("groupId", testData.getGroupId().toString());

        mockMvc.perform(post("/student/save").params(multiValueMap).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/student")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void saveStudent_shouldRedirectToRegistrationPage_whenGroupEmpty(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", testData.getStudentFirstName());
        multiValueMap.add("lastName", testData.getStudentLastName());
        multiValueMap.add("email", testData.getEmail());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());
        multiValueMap.add("groupId", "0");

        mockMvc.perform(post("/student/save").params(multiValueMap).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/addUser"),
                        model().attributeExists("emptyGroup"),
                        content().string(containsString("Group is required."))
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void save_shouldRedirectToRegistrationPage_whenReceivedEmailIsInvalid(String role) throws Exception {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(testItems.getCompliteUser()));

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", testData.getStudentFirstName());
        multiValueMap.add("lastName", testData.getStudentLastName());
        multiValueMap.add("email", testData.getEmail());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());
        multiValueMap.add("groupId", String.valueOf(testData.getGroupId()));

        mockMvc.perform(post("/student/save").params(multiValueMap).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/addUser"),
                        model().hasErrors(),
                        content().string(containsString("There is already an account registered with that email"))
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void save_shouldDisplayedErrorMessageOnRegistrationPage_whenReceivedInvalidData(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", "");
        multiValueMap.add("lastName", "");
        multiValueMap.add("email", "");
        multiValueMap.add("password", "");
        multiValueMap.add("confirmPassword", "");
        multiValueMap.add("groupId", "0");

        mockMvc.perform(post("/student/save").params(multiValueMap).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().hasErrors(),
                        view().name("user/addUser"),
                        content().string(containsString("First name is required.")),
                        content().string(containsString("First name must be between 2 and 15 characters.")),
                        content().string(containsString("Last name is required.")),
                        content().string(containsString("Last name must be between 2 and 20 characters.")),
                        content().string(containsString("Email address is required.")),
                        content().string(containsString("Password must be between 8 and 16 characters.")),
                        content().string(containsString("Password is required.")),
                        content().string(containsString("Confirm password is required.")),
                        content().string(containsString("Group is required."))
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void saveStudent_shouldReturnForbiddenStatus_whenLoggedUserLacksAccess(String role) throws Exception {
        mockMvc.perform(post("/student/save").with(testUser(role))).andDo(print())
                .andExpect(
                        status().is4xxClientError());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void viewDetails_shouldReturnUserDetailsPage_whenLoggedUserHasAccess(String role) throws Exception {
        when(studentService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());
        when(studentService.getStudentGroup(anyLong())).thenReturn(testItems.getStudentGroupDto());

        mockMvc.perform(get("/student/details/{id}", testData.getStudentId()).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().attributeExists("user"),
                        model().attributeExists("path"),
                        model().attributeDoesNotExist("isMyDetails"),
                        model().attributeExists("userDto"),
                        content().string(containsString(testData.getUserFirstName())),
                        content().string(containsString(testData.getUserFirstName())),
                        content().string(containsString(testData.getUserEmail())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void viewDetails_returnStatusForbidden_whenLoggedUserLacksAccess(String role) throws Exception {
        mockMvc.perform(get("/student/details/{id}", testData.getStudentId()).with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "student"})
    void updateNames_shouldRedirectToUserPage_whenReceivedValidNames(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getStudentId().toString());
        multiValueMap.add("firstName", testData.getStudentFirstName());
        multiValueMap.add("lastName", testData.getStudentLastName());
        multiValueMap.add("email", testData.getStudentEmail());

        mockMvc.perform(post("/student/update-names").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/student")
                );
    }


    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "student"})
    void updateNames_shouldDisplaysErrorMessage_whenReceivedInvalidNames(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getStudentId().toString());
        multiValueMap.add("firstName", "");
        multiValueMap.add("lastName", "");
        multiValueMap.add("email", testData.getStudentEmail());

        mockMvc.perform(post("/student/update-names").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().hasErrors(),
                        content().string(containsString("First name is required.")),
                        content().string(containsString("First name must be between 2 and 15 characters.")),
                        content().string(containsString("Last name is required.")),
                        content().string(containsString("Last name must be between 2 and 20 characters.")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "student"})
    void updateEmail_shouldRedirectToUserPage_whenReceivedValidEmail(String role) throws Exception {
        when(studentService.getDetails(testData.getStudentId())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getStudentId().toString());
        multiValueMap.add("firstName", testData.getStudentFirstName());
        multiValueMap.add("lastName", testData.getStudentLastName());
        multiValueMap.add("email", testData.getNewEmail());

        mockMvc.perform(post("/student/update-email").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/student"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "student"})
    void updateEmail_shouldOutputErrorMessage_whenReceivedEmailIsActualUserEmail(String role) throws Exception {
        when(studentService.getDetails(testData.getStudentId())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getStudentId().toString());
        multiValueMap.add("firstName", testData.getStudentFirstName());
        multiValueMap.add("lastName", testData.getStudentLastName());
        multiValueMap.add("email", testData.getUserEmail());

        mockMvc.perform(post("/student/update-email").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().hasErrors(),
                        content().string(containsString("Given email is current user email.")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void editStudentGroup_shouldReturnEditGroupPage_whenLoggedUserHasAccess(String role) throws Exception {
        when(studentService.getStudentGroup(anyLong())).thenReturn(testItems.getStudentGroupDto());
        when(groupService.getAll()).thenReturn(testItems.getGroups());

        mockMvc.perform(get("/student/edit-group/{id}", testData.getStudentId()).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("student/feature/editGroup"),
                        model().attributeExists("studentGroupDto"),
                        model().attributeExists("groups"),
                        content().string(containsString(testData.getStudentId().toString())),
                        content().string(containsString(testData.getGroupId().toString())),
                        content().string(containsString(testData.getGroupName()))
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void editStudentGroup_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(get("/student/edit-group/{id}", testData.getStudentId()).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void updateStudentGroup_shouldReturnStudentPage_whenLoggedUserHasAccess(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getStudentId().toString());
        multiValueMap.add("groupId", testData.getSecondGroupId().toString());
        multiValueMap.add("groupName", testData.getSecondGroupName());

        mockMvc.perform(post("/student/update-group").params(multiValueMap).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/student"));
    }

    @Test
    void updateStudentGroup_shouldReturnForbiddenStatus_whenLoggedUserLackAccess() throws Exception {
        mockMvc.perform(post("/student/update-group").with(testUser("teacher"))).andDo(print())
                .andExpectAll(
                        status().is4xxClientError());
    }

    @Test
    void updatePassword_shouldReturnUserDetailsPage_whenPasswordChangeSucceeded() throws Exception {
        when(studentService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", testData.getOldPassword());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/student/update-password").params(multiValueMap).with(testUser("student"))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/student/details?success=true"));
    }

    @Test
    void updatePassword_shouldOutputErrorMessageOnUserDetailsPage_whenOldPasswordIsInvalid() throws Exception {
        when(studentService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(studentService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", testData.getOldPassword());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/student/update-password").params(multiValueMap).with(testUser("student"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().hasErrors(),
                        view().name("user/userDetails"),
                        model().attributeExists("userDto"),
                        model().attributeExists("isMyDetails"),
                        content().string(containsString("Old password is incorrect."))
                );
    }

    @Test
    void updatePassword_shouldOutputErrorMessageOnUserDetailsPage_whenPasswordNotMatchConfirmPassword() throws Exception {
        when(studentService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(studentService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", testData.getOldPassword());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword() + "2");

        mockMvc.perform(post("/student/update-password").params(multiValueMap).with(testUser("student"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().hasErrors(),
                        model().attributeExists("userDto"),
                        model().attributeExists("isMyDetails"),
                        content().string(containsString("Oops! Looks like your passwords dont match. Double-check and try again."))
                );
    }

    @Test
    void updatePassword_shouldOutputErrorMessageOnUserDetailsPage_whenReceivedEmptyForm() throws Exception {
        when(studentService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(studentService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", "");
        multiValueMap.add("password", "");
        multiValueMap.add("confirmPassword", "");

        mockMvc.perform(post("/student/update-password").params(multiValueMap).with(testUser("student"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().hasErrors(),
                        model().attributeExists("userDto"),
                        model().attributeExists("isMyDetails"),
                        content().string(containsString("Old password is incorrect.")),
                        content().string(containsString("Old password is required.")),
                        content().string(containsString("Password must be between 8 and 16 characters.")),
                        content().string(containsString("Password is required.")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "teacher"})
    void updateStudentPassword_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(post("/student/update-password").with(testUser(role))).andDo(print())
                .andExpect(
                        status().isForbidden());
    }

    @Test
    void viewMyDetails_shouldReturnUserDetailsPage_whenLoggedUserHasAccess() throws Exception {
        when(studentService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(studentService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        mockMvc.perform(get("/student/details").with(testUser("student"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().attributeExists("user"),
                        model().attributeExists("path"),
                        model().attributeExists("userDto"),
                        model().attributeExists("isMyDetails"),
                        model().attributeDoesNotExist("studentGroupDto"),
                        content().string(containsString(testData.getUserId().toString())),
                        content().string(containsString(testData.getUserFirstName())),
                        content().string(containsString(testData.getUserLastName())),
                        content().string(containsString(testData.getUserEmail())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "teacher"})
    void viewMyDetails_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(get("/student/details").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }

    @Test
    void deleteForm_shouldReturnDeleteUserPage_whenLoggedUserHasAccess() throws Exception {
        when(studentService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        mockMvc.perform(get("/student/remove/{id}", testData.getUserId()).with(testUser("admin"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/deleteUser"),
                        model().attributeExists("path"),
                        model().attributeExists("userDto"),
                        content().string(containsString(testData.getUserId().toString())),
                        content().string(containsString(testData.getUserFirstName())),
                        content().string(containsString(testData.getUserLastName())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "staff", "teacher"})
    void deleteForm_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(get("/student/remove/{id}", testData.getStudentId()).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }

    @Test
    void delete_shouldReturnForbiddenStatus_whenLoggedUserLackAccess() throws Exception {
        mockMvc.perform(post("/student/delete/{id}", testData.getStudentId())
                        .with(testUser("admin"))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/student"));
    }
}