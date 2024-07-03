package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.persistence.entity.Admin;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.persistence.repository.AdminRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AdminServiceImpl.class)
class AdminServiceImplTest {
    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();

    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AdminServiceImpl adminService;

    @Test
    void updateNames_shouldReturnUpdatedInstance_whenNamesUpdatedSuccessfully(){
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyAdmin()));

        adminService.updateNames(testItems.getUserDetails());

        var argumentCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(argumentCaptor.capture());
        Admin captorValue = argumentCaptor.getValue();
        assertAll(() -> {
            assertThat(captorValue.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(captorValue.getLastName()).isEqualTo(testData.getUserLastName());
        });
    }

    @Test
    void updateEmail_shouldReturnUpdatedInstance_whenEmailUpdatedSuccessfully(){
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyAdmin()));

        adminService.updateEmail(testItems.getUserDetails());

        var argumentCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getEmail()).isEqualTo(testData.getUserEmail());
    }

    @Test
    void getDetails_shouldReturnUserDtoInstance_whenUserIsFound() {
        when(adminRepository.getUserDataById(anyLong())).thenReturn(testItems.getUserDetails());

        adminService.getDetails(testData.getUserId());

        verify(adminRepository).getUserDataById(testData.getUserId());
    }

    @Test
    void updatePassword_shouldReturnUpdatedInstance_whenPasswordUpdatedSuccessfully(){
        ChangePasswordForm passwordForm = testItems.getChangePasswordForm();
        passwordForm.setId(testData.getAdminId());

        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyAdmin()));
        when(passwordEncoder.encode(anyString())).thenReturn(testItems.getEncodedPassword2());

        adminService.updatePassword(passwordForm);

        verify(adminRepository).findById(testData.getAdminId());
        verify(passwordEncoder).encode(testItems.getPassword());

        var argumentCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getPassword()).isEqualTo(testItems.getEncodedPassword2());
    }

    @Test
    void deleteUser_shouldRemoveUser_whenUserIsFounded() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyAdmin()));

        adminService.deleteUser(testData.getAdminId());

        verify(adminRepository).findById(testData.getAdminId());

        var argumentCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).delete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId()).isEqualTo(testData.getAdminId());
    }

    @Test
    void deleteUser_shouldThrowEntityNotFoundException_whenUserIsNotFounded() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adminService.deleteUser(testData.getAdminId()));

        verify(adminRepository).findById(testData.getAdminId());
    }

    @Test
    void save_shouldReturnAdminInstanceWithId_whenIsSavedSuccessfully() {
        when(passwordEncoder.encode(anyString())).thenReturn(testItems.getEncodedPassword());

        adminService.saveUser(testItems.userRegistrationForm());

        var argumentCapture = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(argumentCapture.capture());
        Admin captureValue = argumentCapture.getValue();
        assertAll(() -> {
            assertThat(captureValue.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(captureValue.getLastName()).isEqualTo(testData.getUserLastName());
            assertThat(captureValue.getEmail()).isEqualTo(testData.getUserEmail());
            assertThat(captureValue.getPassword()).isEqualTo(testItems.getEncodedPassword());
        });
    }

    @Test
    void getById_shouldReturnAdminInstance_whenIdIsFound() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyAdmin()));

        assertThat(adminService.getById(testData.getAdminId())).isNotNull();

        verify(adminRepository).findById(testData.getAdminId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adminService.getById(testData.getAdminId()));

        verify(adminRepository).findById(testData.getAdminId());
    }

    @Test
    void getAll_shouldReturnListOfAdmins_whenIsInvoke() {
        when(adminRepository.findAll()).thenReturn(Collections.singletonList(testItems.getFullyAdmin()));

        assertThat(adminService.getAll()).hasSize(1);

        verify(adminRepository).findAll();
    }

    @Test
    void getUserByEmail_shouldReturnUserInstance_whenUserEmailFound(){
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(testItems.getCompliteUser()));

        User result = adminService.getUserByEmail(testData.getUserEmail());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(User.class);
            assertThat(result.getId()).isEqualTo(testData.getUserId());
            assertThat(result.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(result.getLastName()).isEqualTo(testData.getUserLastName());
            assertThat(result.getEmail()).isEqualTo(testData.getUserEmail());
        });
        verify(userRepository).findUserByEmail(testData.getUserEmail());
    }
}