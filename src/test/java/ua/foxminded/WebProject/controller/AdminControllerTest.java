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
import ua.foxminded.WebProject.service.AdminService;
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
@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest {

    private final TestItems testItems = new TestItems();
    private final TestData testData = new TestData();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;


    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void adminPage(String role) throws Exception {
        mockMvc.perform(get("/admin").with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        view().name("user/user"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "student"})
    void adminPage_shouldReturnForbidden_whenUserLacksAccess(String role) throws Exception {
        mockMvc.perform(get("/admin").with(testUser(role)))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"staff", "admin"})
    void addUser_shouldReturnAddUserView_whenLoggedUserHasAccess(String role) throws Exception {
        mockMvc.perform(get("/admin/addUser").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("registrationForm"),
                        model().attributeExists("user"),
                        model().attributeExists("path"),
                        view().name("user/addUser"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "student"})
    void addUser_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(get("/admin/addUser").with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void saveUser_shouldRedirectToUserPage_whenReceivedDataIsValid() throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", testData.getUserFirstName());
        multiValueMap.add("lastName", testData.getUserLastName());
        multiValueMap.add("email", testData.getUserEmail());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/admin/save").params(multiValueMap).with(testUser("admin"))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/admin")
                );
    }

    @Test
    void save_shouldRedirectToRegistrationPage_whenReceivedEmailIsInvalid() throws Exception {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(testItems.getCompliteUser()));

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", testData.getStaffFirstName());
        multiValueMap.add("lastName", testData.getStaffLastName());
        multiValueMap.add("email", testData.getUserEmail());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/admin/save").params(multiValueMap).with(testUser("admin"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/addUser"),
                        model().hasErrors(),
                        content().string(containsString("There is already an account registered with that email"))
                );
    }

    @Test
    void save_shouldDisplayedErrorMessageOnRegistrationPage_whenReceivedInvalidData() throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("firstName", "");
        multiValueMap.add("lastName", "");
        multiValueMap.add("email", "");
        multiValueMap.add("password", "");
        multiValueMap.add("confirmPassword", "");

        mockMvc.perform(post("/admin/save").params(multiValueMap).with(testUser("admin"))).andDo(print())
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
        when(adminService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        mockMvc.perform(get("/admin/details/{id}", testData.getAdminId()).with(testUser(role))).andDo(print())
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
        mockMvc.perform(get("/admin/details/{id}", testData.getAdminId()).with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void updateNames_shouldRedirectToUserPage_whenReceivedValidNames(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getAdminId().toString());
        multiValueMap.add("firstName", testData.getStaffFirstName());
        multiValueMap.add("lastName", testData.getStaffLastName());
        multiValueMap.add("email", testData.getAdminEmail());

        mockMvc.perform(post("/admin/update-names").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/admin")
                );
    }


    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void updateNames_shouldDisplaysErrorMessage_whenReceivedInvalidNames(String role) throws Exception {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getAdminId().toString());
        multiValueMap.add("firstName", "");
        multiValueMap.add("lastName", "");
        multiValueMap.add("email", testData.getAdminEmail());

        mockMvc.perform(post("/admin/update-names").with(testUser(role)).params(multiValueMap)).andDo(print())
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
    @ValueSource(strings = {"admin", "staff"})
    void updateEmail_shouldRedirectToUserPage_whenReceivedValidEmail(String role) throws Exception {
        when(adminService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getAdminId().toString());
        multiValueMap.add("firstName", testData.getAdminFirstName());
        multiValueMap.add("lastName", testData.getAdminLastName());
        multiValueMap.add("email", testData.getNewEmail());

        mockMvc.perform(post("/admin/update-email").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/admin"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void updateEmail_shouldOutputErrorMessage_whenReceivedEmailIsActualUserEmail(String role) throws Exception {
        when(adminService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("id", testData.getAdminId().toString());
        multiValueMap.add("firstName", testData.getAdminFirstName());
        multiValueMap.add("lastName", testData.getAdminLastName());
        multiValueMap.add("email", testData.getUserEmail());

        mockMvc.perform(post("/admin/update-email").with(testUser(role)).params(multiValueMap)).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().hasErrors(),
                        content().string(containsString("Given email is current user email.")));
    }

    @Test
    void updatePassword_shouldReturnUserDetailsPage_whenPasswordChangeSucceeded() throws Exception {
        when(adminService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", testData.getOldPassword());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/admin/update-password").params(multiValueMap).with(testUser("admin"))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/admin/details?success=true"));
    }

    @Test
    void updatePassword_shouldOutputErrorMessageOnUserDetailsPage_whenOldPasswordIsInvalid() throws Exception {
        when(adminService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(adminService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", testData.getOldPassword());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword());

        mockMvc.perform(post("/admin/update-password").params(multiValueMap).with(testUser("admin"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().hasErrors(),
                        model().attributeExists("userDto"),
                        model().attributeExists("isMyDetails"),
                        view().name("user/userDetails"),
                        content().string(containsString("Old password is incorrect.")));
    }

    @Test
    void updatePassword_shouldOutputErrorMessageOnUserDetailsPage_whenPasswordNotMatchConfirmPassword() throws Exception {
        when(adminService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(adminService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", testData.getOldPassword());
        multiValueMap.add("password", testItems.getPassword());
        multiValueMap.add("confirmPassword", testItems.getPassword() + "2");

        mockMvc.perform(post("/admin/update-password").params(multiValueMap).with(testUser("admin"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().attributeExists("userDto"),
                        model().attributeExists("isMyDetails"),
                        model().hasErrors(),
                        content().string(containsString("Oops! Looks like your passwords dont match. Double-check and try again."))
                );
    }

    @Test
    void updatePassword_shouldOutputErrorMessageOnUserDetailsPage_whenReceivedEmptyForm() throws Exception {
        when(adminService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(adminService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("oldPassword", "");
        multiValueMap.add("password", "");
        multiValueMap.add("confirmPassword", "");

        mockMvc.perform(post("/admin/update-password").params(multiValueMap).with(testUser("admin"))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("user/userDetails"),
                        model().attributeExists("userDto"),
                        model().attributeExists("isMyDetails"),
                        model().hasErrors(),
                        content().string(containsString("Old password is incorrect.")),
                        content().string(containsString("Old password is required.")),
                        content().string(containsString("Password must be between 8 and 16 characters.")),
                        content().string(containsString("Password is required.")));
    }

    @Test
    void viewMyDetails_shouldReturnUserDetailsPage_whenLoggedUserHasAccess() throws Exception {
        when(adminService.getUserByEmail(anyString())).thenReturn(testItems.getCompliteUser());
        when(adminService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        mockMvc.perform(get("/admin/details").with(testUser("admin"))).andDo(print())
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
    @ValueSource(strings = {"staff", "student", "teacher"})
    void viewMyDetails_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(get("/admin/details").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }

    @Test
    void deleteForm_shouldReturnDeleteUserPage_whenLoggedUserHasAccess() throws Exception {
        when(adminService.getDetails(anyLong())).thenReturn(testItems.getUserDetails());

        mockMvc.perform(get("/admin/remove/{id}", testData.getUserId()).with(testUser("admin"))).andDo(print())
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
        mockMvc.perform(get("/admin/remove/{id}", testData.getAdminId()).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "staff", "teacher"})
    void delete_shouldReturnForbiddenStatus_whenLoggedUserLackAccess(String role) throws Exception {
        mockMvc.perform(post("/admin/delete/{id}", testData.getAdminId()).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }
}