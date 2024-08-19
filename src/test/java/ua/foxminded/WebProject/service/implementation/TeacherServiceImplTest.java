package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.persistence.repository.TeacherRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TeacherServiceImpl.class)
class TeacherServiceImplTest {
    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();

    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TeacherServiceImpl teacherService;


    @Test
    void updateNames_shouldReturnUpdatedInstance_whenNamesUpdatedSuccessfully() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyTeacher()));

        teacherService.updateNames(testItems.getUserDetails());

        var argumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(argumentCaptor.capture());
        Teacher teacher = argumentCaptor.getValue();
        assertAll(() -> {
            assertThat(teacher.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(teacher.getLastName()).isEqualTo(testData.getUserLastName());
        });
    }

    @Test
    void updateEmail_shouldReturnUpdatedInstance_whenEmailUpdatedSuccessfully() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyTeacher()));

        teacherService.updateEmail(testItems.getUserDetails());

        var argumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getEmail()).isEqualTo(testData.getUserEmail());
    }

    @Test
    void getUserByEmail_shouldReturnUserInstance_whenUserEmailFound() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(testItems.getCompliteUser()));

        User result = teacherService.getUserByEmail(testData.getUserEmail());

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

    @Test
    void getDetails_shouldReturnUserDtoInstance_whenUserIsFound() {
        when(teacherRepository.getUserDataById(anyLong())).thenReturn(testItems.getUserDetails());

        teacherService.getDetails(testData.getUserId());

        verify(teacherRepository).getUserDataById(testData.getUserId());
    }

    @Test
    void updatePassword_shouldReturnUpdatedInstance_whenPasswordUpdatedSuccessfully() {
        ChangePasswordForm passwordForm = testItems.getChangePasswordForm();
        passwordForm.setId(testData.getTeacherId());

        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyTeacher()));
        when(passwordEncoder.encode(anyString())).thenReturn(testItems.getEncodedPassword2());

        teacherService.updatePassword(passwordForm);

        verify(teacherRepository).findById(testData.getTeacherId());
        verify(passwordEncoder).encode(testItems.getPassword());

        var argumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(argumentCaptor.capture());
        Teacher captorValue = argumentCaptor.getValue();
        assertThat(captorValue.getPassword()).isEqualTo(testItems.getEncodedPassword2());
    }

    @Test
    void save_shouldReturnTeacherInstanceWithId_whenIsSavedSuccessfully() {
        when(passwordEncoder.encode(anyString())).thenReturn(testItems.getEncodedPassword());
        teacherService.saveUser(testItems.userRegistrationForm());

        var argumentCapture = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(argumentCapture.capture());
        Teacher captureValue = argumentCapture.getValue();
        assertAll(() -> {
            assertThat(captureValue.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(captureValue.getLastName()).isEqualTo(testData.getUserLastName());
            assertThat(captureValue.getEmail()).isEqualTo(testData.getUserEmail());
            assertThat(captureValue.getPassword()).isEqualTo(testItems.getEncodedPassword());
        });
    }

    @Test
    void deleteUser_shouldRemoveUser_whenUserIsFounded() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyTeacher()));

        teacherService.deleteUser(testData.getTeacherId());

        verify(teacherRepository).findById(testData.getTeacherId());
        var argumentCaptor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).delete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId()).isEqualTo(testData.getTeacherId());
    }

    @Test
    void deleteUser_shouldThrowEntityNotFoundException_whenUserIsNotFounded() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teacherService.deleteUser(testData.getTeacherId()));

        verify(teacherRepository).findById(testData.getTeacherId());
    }

    @Test
    void getAll_shouldReturnListOfTeachers_whenIsInvoke() {
        when(teacherRepository.findAll()).thenReturn(testItems.getTeachers());

        assertThat(teacherService.getAll()).hasSize(8);

        verify(teacherRepository).findAll();
    }

    @Test
    void getById_shouldReturnTeacherInstance_whenIdIsFound() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyTeacher()));

        assertThat(teacherService.getById(testData.getTeacherId())).isNotNull();

        verify(teacherRepository).findById(testData.getTeacherId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teacherService.getById(testData.getTeacherId()));

        verify(teacherRepository).findById(testData.getTeacherId());
    }
}