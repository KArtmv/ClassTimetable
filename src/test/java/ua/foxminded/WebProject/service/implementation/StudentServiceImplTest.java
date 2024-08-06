package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.StudentRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = StudentServiceImpl.class)
class StudentServiceImplTest {

    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @MockBean
    private GroupService groupService;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private StudentServiceImpl studentService;

    @Test
    void updateNames() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyStudent()));

        studentService.updateNames(testItems.getUserDetails());

        var argumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(argumentCaptor.capture());
        Student student = argumentCaptor.getValue();
        assertAll(() -> {
            assertThat(student.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(student.getLastName()).isEqualTo(testData.getUserLastName());
        });
    }

    @Test
    void updateEmail() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyStudent()));

        studentService.updateEmail(testItems.getUserDetails());

        var argumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getEmail()).isEqualTo(testData.getUserEmail());
    }

    @Test
    void getUserByEmail() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.ofNullable(testItems.getCompliteUser()));

        User result = studentService.getUserByEmail(testData.getUserEmail());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(User.class);
            assertThat(result.getId()).isEqualTo(testData.getUserId());
            assertThat(result.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(result.getLastName()).isEqualTo(testData.getUserLastName());
            assertThat(result.getEmail()).isEqualTo(testData.getUserEmail());
        });
    }

    @Test
    void getStudentGroup_shouldReturnStudentGroup_whenIsFound() {
        when(studentRepository.getStudentGroup(anyLong())).thenReturn(testItems.getStudentGroupDto());

        StudentGroupDto result = studentService.getStudentGroup(testData.getStudentId());
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(testData.getStudentId());
            assertThat(result.groupId()).isEqualTo(testData.getGroupId());
            assertThat(result.groupName()).isEqualTo(testData.getGroupName());
        });

        verify(studentRepository).getStudentGroup(testData.getStudentId());
    }

    @Test
    void updateStudentGroup_shouldUpdateStudentGroup_whenIsFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItems.getFullyStudent()));
        when(groupService.getById(anyLong())).thenReturn(testItems.getSecondGroup());

        studentService.updateStudentGroup(testItems.getStudentGroupDto2());

        verify(studentRepository).findById(testData.getStudentId());
        verify(groupService).getById(testData.getSecondGroupId());
        var argumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(argumentCaptor.capture());
        Student captorValue = argumentCaptor.getValue();
        assertThat(captorValue.getGroup().getId()).isEqualTo(testData.getSecondGroupId());
    }

    @Test
    void getById_shouldReturnStudentInstance_whenIsFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyStudent()));

        assertThat(studentService.getById(testData.getStudentId())).isNotNull();

        verify(studentRepository).findById(testData.getStudentId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> studentService.getById(testData.getStudentId()));

        verify(studentRepository).findById(testData.getStudentId());
    }

    @Test
    void save_shouldReturnAdminInstanceWithId_whenIsSavedSuccessfully() {
        when(passwordEncoder.encode(anyString())).thenReturn(testItems.getEncodedPassword());

        studentService.saveUser(testItems.userRegistrationForm());

        var argumentCapture = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(argumentCapture.capture());
        Student captureValue = argumentCapture.getValue();
        assertAll(() -> {
            assertThat(captureValue.getFirstName()).isEqualTo(testData.getUserFirstName());
            assertThat(captureValue.getLastName()).isEqualTo(testData.getUserLastName());
            assertThat(captureValue.getEmail()).isEqualTo(testData.getUserEmail());
            assertThat(captureValue.getPassword()).isEqualTo(testItems.getEncodedPassword());
        });
    }

    @Test
    void getAll_shouldReturnListOfStudent_whenIsInvoke() {
        when(studentRepository.findAll()).thenReturn(testItems.getStudents());

        assertThat(studentService.getAll()).hasSize(5);

        verify(studentRepository).findAll();
    }

    @Test
    void getDetails_shouldReturnUserDtoInstance_whenUserIsFound() {
        when(studentRepository.getUserDataById(anyLong())).thenReturn(testItems.getUserDetails());

        studentService.getDetails(testData.getUserId());

        verify(studentRepository).getUserDataById(testData.getUserId());
    }

    @Test
    void updatePassword() {
        ChangePasswordForm passwordForm = testItems.getChangePasswordForm();
        passwordForm.setId(testData.getStudentId());

        when(studentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItems.getFullyStudent()));
        when(passwordEncoder.encode(anyString())).thenReturn(testItems.getEncodedPassword2());

        studentService.updatePassword(passwordForm);

        verify(studentRepository).findById(testData.getStudentId());
        verify(passwordEncoder).encode(testItems.getPassword());

        var argumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(argumentCaptor.capture());
        Student captorValue = argumentCaptor.getValue();
        assertThat(captorValue.getPassword()).isEqualTo(testItems.getEncodedPassword2());
    }

    @Test()
    void deleteUser_shouldThrowEntityNotFoundException_whenStudentIsNotFound() {
        when(studentRepository.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> studentService.deleteUser(testData.getStudentId()));

        verify(studentRepository).findById(testData.getStudentId());
    }


    @Test
    void deleteUser_shouldRemoveStudent_whenIsFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getFullyStudent()));

        studentService.deleteUser(testData.getStudentId());

        verify(studentRepository).findById(testData.getStudentId());
        verify(studentRepository).delete(testItems.getFullyStudent());
    }

    @Test
    void getStudentsByGroupId_shouldReturnStudentsNamesList_whenIsFound() {
        when(studentRepository.getAllByGroupId(anyLong())).thenReturn(Collections.singletonList(testItems.userDto(testItems.getFullyStudent())));

        assertThat(studentService.getStudentsByGroupId(testData.getGroupId())).hasSize(1);

        verify(studentRepository).getAllByGroupId(testData.getGroupId());
    }
}