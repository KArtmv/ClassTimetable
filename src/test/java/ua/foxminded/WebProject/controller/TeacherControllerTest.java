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
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.service.TeacherService;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    private final TestItems testItems = new TestItems();
    private final TestData testData = new TestData();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;


    @ParameterizedTest
    @ValueSource(strings = {"admin", "teacher", "staff"})
    void userPage_shouldReturnPageWithListOfUser_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(teacherService.getAll()).thenReturn(testItems.getTeachers());

        mockMvc.perform(get("/teacher").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("users"),
                        view().name("user/user"),
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
    void userPage_shouldReturnForbiddenStatus_whenLoginUserWithoutAbleAccess() throws Exception {
        this.mockMvc.perform(get("/teacher").with(testUser("student")))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void addUser_shouldReturnAddUserView_whenLoggedUserHasAccess(String role) throws Exception {
        mockMvc.perform(get("/teacher/addUser").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("registrationForm"),
                        model().attributeExists("path"),
                        view().name("user/addUser"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "student"})
    void addUser_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(get("/teacher/addUser").with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void saveUser_shouldRedirectToUserPage_whenReceivedDataIsValid(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", testData.getTeacherFirstName());
        multiValueMap.add("lastName", testData.getTeacherLastName());
        multiValueMap.add("email", testData.getTeacherEmail());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/teacher/save").params(multiValueMap).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/teacher")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void save_shouldRedirectToRegistrationPage_whenReceivedEmailIsInvalid(String role) throws Exception {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(testItems.getCompliteUser()));

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", testData.getStaffFirstName());
        multiValueMap.add("lastName", testData.getStaffLastName());
        multiValueMap.add("email", testData.getStaffEmail());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/teacher/save").params(multiValueMap).with(testUser(role))).andDo(print())
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

        mockMvc.perform(post("/teacher/save").params(multiValueMap).with(testUser(role))).andDo(print())
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
                        content().string(containsString("Confirm password is required.")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void viewDetails_shouldReturnUserDetailsPage_whenLoggedUserHasAccess(String role) throws Exception {
        when(teacherService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        mockMvc.perform(get("/teacher/details/{id}", testData.getTeacherId()).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().attributeExists("path"),
                        model().attributeExists("userDto"),
                        content().string(containsString(testData.getUserFirstName())),
                        content().string(containsString(testData.getUserFirstName())),
                        content().string(containsString(testData.getUserEmail())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void viewDetails_returnStatusForbidden_whenLoggedUserLacksAccess(String role) throws Exception {
        mockMvc.perform(get("/teacher/details/{id}", testData.getTeacherId()).with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "teacher"})
    void updateNames_shouldRedirectToUserPage_whenReceivedValidNames(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getTeacherId().toString());
        multiValueMap.add("firstName", testData.getStaffFirstName());
        multiValueMap.add("lastName", testData.getStaffLastName());
        multiValueMap.add("email", testData.getTeacherEmail());

        mockMvc.perform(post("/teacher/update-names").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/teacher")
                );
    }


    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "teacher"})
    void updateNames_shouldDisplaysErrorMessage_whenReceivedInvalidNames(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getTeacherId().toString());
        multiValueMap.add("firstName", "");
        multiValueMap.add("lastName", "");
        multiValueMap.add("email", testData.getTeacherEmail());

        mockMvc.perform(post("/teacher/update-names").with(testUser(role)).params(multiValueMap)).andDo(print())
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
    @ValueSource(strings = {"admin", "staff", "teacher"})
    void updateEmail_shouldRedirectToUserPage_whenReceivedValidEmail(String role) throws Exception {
        when(teacherService.getDetails(testData.getTeacherId())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getTeacherId().toString());
        multiValueMap.add("firstName", testData.getStaffFirstName());
        multiValueMap.add("lastName", testData.getStaffLastName());
        multiValueMap.add("email", testData.getNewEmail());

        mockMvc.perform(post("/teacher/update-email").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/teacher"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "teacher"})
    void updateEmail_shouldOutputErrorMessage_whenReceivedEmailIsActualUserEmail(String role) throws Exception {
        when(teacherService.getDetails(testData.getTeacherId())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getTeacherId().toString());
        multiValueMap.add("firstName", testData.getTeacherFirstName());
        multiValueMap.add("lastName", testData.getTeacherLastName());
        multiValueMap.add("email", testData.getUserEmail());

        mockMvc.perform(post("/teacher/update-email").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().hasErrors(),
                        content().string(containsString("Given email is current user email.")));
    }

    @Test
    void updatePassword_shouldReturnUserDetailsPage_whenPasswordChangeSucceeded() throws Exception {
        when(teacherService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", testData.getOldPassword());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/teacher/update-password").params(multiValueMap).with(testUser("teacher"))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/teacher/details?success=true"));
    }

    @Test
    void updatePassword_shouldOutputErrorMessageOnUserDetailsPage_whenOldPasswordIsInvalid() throws Exception {
        when(teacherService.getUserByEmail(anyString())).thenReturn(testItems.getFullyTeacher());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(teacherService.getDetails(anyLong())).thenReturn(testItems.userDto(testItems.getFullyTeacher()));

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", testData.getOldPassword());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/teacher/update-password").params(multiValueMap).with(testUser("teacher"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().hasErrors(),
                        model().attributeExists("userDto"),
                        view().name("user/userDetails"),
                        content().string(containsString("Old password is incorrect.")));
    }

    @Test
    void updatePassword_shouldOutputErrorMessageOnUserDetailsPage_whenPasswordNotMatchConfirmPassword() throws Exception {
        when(teacherService.getUserByEmail(anyString())).thenReturn(testItems.getFullyTeacher());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(teacherService.getDetails(anyLong())).thenReturn(testItems.userDto(testItems.getFullyTeacher()));

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", testData.getOldPassword());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword() + "2");

        mockMvc.perform(post("/teacher/update-password").params(multiValueMap).with(testUser("teacher"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().attributeExists("userDto"),
                        model().hasErrors(),
                        content().string(containsString("Oops! Looks like your passwords dont match. Double-check and try again."))
                );
    }

    @Test
    void updatePassword_shouldOutputErrorMessageOnUserDetailsPage_whenReceivedEmptyForm() throws Exception {
        when(teacherService.getUserByEmail(anyString())).thenReturn(testItems.getFullyTeacher());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(teacherService.getDetails(anyLong())).thenReturn(testItems.userDto(testItems.getFullyTeacher()));

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", "");
        multiValueMap.add("password", "");
        multiValueMap.add("confirmPassword", "");

        mockMvc.perform(post("/teacher/update-password").params(multiValueMap).with(testUser("teacher"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().attributeExists("userDto"),
                        model().hasErrors(),
                        content().string(containsString("Old password is incorrect.")),
                        content().string(containsString("Old password is required.")),
                        content().string(containsString("Password must be between 8 and 16 characters.")),
                        content().string(containsString("Password is required.")));
    }

    @Test
    void viewMyDetails_shouldReturnUserDetailsPage_whenLoggedUserHasAccess() throws Exception {
        when(teacherService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(teacherService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        mockMvc.perform(get("/teacher/details").with(testUser("teacher"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().attributeExists("path"),
                        model().attributeExists("userDto"),
                        model().attributeDoesNotExist("studentGroupDto"),
                        content().string(containsString(testData.getUserId().toString())),
                        content().string(containsString(testData.getUserFirstName())),
                        content().string(containsString(testData.getUserLastName())),
                        content().string(containsString(testData.getUserEmail())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "student", "staff"})
    void viewMyDetails_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(get("/teacher/details").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }

    @Test
    void deleteForm_shouldReturnDeleteUserPage_whenLoggedUserHasAccess() throws Exception {
        when(teacherService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        mockMvc.perform(get("/teacher/remove/{id}", testData.getUserId()).with(testUser("admin"))).andDo(print())
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
        mockMvc.perform(get("/teacher/remove/{id}", testData.getTeacherId()).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "staff", "teacher"})
    void delete_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(post("/teacher/delete/{id}", testData.getTeacherId()).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }
}