package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.persistence.entity.Staff;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.persistence.repository.StaffRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = StaffServiceImpl.class)
class StaffServiceImplTest {

    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();

    @MockBean
    private StaffRepository staffRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private StaffServiceImpl staffService;

    @Test
    void updateNames_shouldReturnUpdatedInstance_whenNamesUpdatedSuccessfully() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyStaff()));

        staffService.updateNames(testItems.getUserDetails());

        var argumentCaptor = ArgumentCaptor.forClass(Staff.class);
        verify(staffRepository).save(argumentCaptor.capture());
        Staff captorValue = argumentCaptor.getValue();
        assertAll(() -> {
            assertThat(captorValue.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(captorValue.getLastName()).isEqualTo(testData.getUserLastName());
        });
    }

    @Test
    void updateEmail_shouldReturnUpdatedInstance_whenEmailUpdatedSuccessfully() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyStaff()));

        staffService.updateEmail(testItems.getUserDetails());

        var argumentCaptor = ArgumentCaptor.forClass(Staff.class);
        verify(staffRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getEmail()).isEqualTo(testData.getUserEmail());
    }

    @Test
    void getDetails_shouldReturnUserDtoInstance_whenUserIsFound() {
        when(staffRepository.getUserDataById(anyLong())).thenReturn(testItems.getUserDetails());

        staffService.getDetails(testData.getUserId());

        verify(staffRepository).getUserDataById(testData.getUserId());
    }

    @Test
    void updatePassword_shouldReturnUpdatedInstance_whenPasswordUpdatedSuccessfully() {
        ChangePasswordForm passwordForm = testItems.getChangePasswordForm();
        passwordForm.setId(testData.getStaffId());

        when(staffRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyStaff()));
        when(passwordEncoder.encode(anyString())).thenReturn(testItems.getEncodedPassword2());

        staffService.updatePassword(passwordForm);

        verify(staffRepository).findById(testData.getStaffId());
        verify(passwordEncoder).encode(testItems.getPassword());

        var argumentCaptor = ArgumentCaptor.forClass(Staff.class);
        verify(staffRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getPassword()).isEqualTo(testItems.getEncodedPassword2());
    }

    @Test
    void deleteUser_shouldRemoveUser_whenUserIsFounded() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyStaff()));

        staffService.deleteUser(testData.getStaffId());

        verify(staffRepository).findById(testData.getStaffId());

        var argumentCaptor = ArgumentCaptor.forClass(Staff.class);
        verify(staffRepository).delete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId()).isEqualTo(testData.getStaffId());
    }

    @Test
    void deleteUser_shouldThrowEntityNotFoundException_whenUserIsNotFounded() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> staffService.deleteUser(testData.getStaffId()));

        verify(staffRepository).findById(testData.getStaffId());
    }

    @Test
    void save_shouldReturnStaffInstanceWithId_whenIsSavedSuccessfully() {
        when(passwordEncoder.encode(anyString())).thenReturn(testItems.getEncodedPassword());

        staffService.saveUser(testItems.userRegistrationForm());

        var argumentCapture = ArgumentCaptor.forClass(Staff.class);
        verify(staffRepository).save(argumentCapture.capture());
        Staff captureValue = argumentCapture.getValue();
        assertAll(() -> {
            assertThat(captureValue.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(captureValue.getLastName()).isEqualTo(testData.getUserLastName());
            assertThat(captureValue.getEmail()).isEqualTo(testData.getUserEmail());
            assertThat(captureValue.getPassword()).isEqualTo(testItems.getEncodedPassword());
        });
    }

    @Test
    void getById_shouldReturnStaffInstance_whenIdIsFound() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyStaff()));

        assertThat(staffService.getById(testData.getStaffId())).isNotNull();

        verify(staffRepository).findById(testData.getStaffId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound() {
        when(staffRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> staffService.getById(testData.getStaffId()));

        verify(staffRepository).findById(testData.getStaffId());
    }

    @Test
    void getAll_shouldReturnListOfStaffs_whenIsInvoke() {
        when(staffRepository.findAll()).thenReturn(Collections.singletonList(testItems.getFullyStaff()));

        assertThat(staffService.getAll()).hasSize(1);

        verify(staffRepository).findAll();
    }

    @Test
    void getUserByEmail_shouldReturnUserInstance_whenUserEmailFound() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(testItems.getCompliteUser()));

        User result = staffService.getUserByEmail(testData.getUserEmail());

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