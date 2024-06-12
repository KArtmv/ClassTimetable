package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.persistence.repository.LessonRepository;
import ua.foxminded.WebProject.persistence.repository.StudentRepository;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    private final TestData testData = new TestData();
    private final TestItems testItems = new TestItems();
    @Mock
    private GroupService groupService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private LessonRepository lessonRepository;
    @InjectMocks
    private StudentServiceImpl studentService;

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
    @Transactional
    void updateStudentGroup_shouldUpdateStudentGroup_whenIsFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItems.getStudent()));
        when(groupService.getById(anyLong())).thenReturn(testItems.getGroup2());
        when(studentRepository.save(any())).thenReturn(testItems.studentWithGroup2());

        Student result = studentService.updateStudentGroup(testItems.getStudentGroupDto2());
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testData.getStudentId());
            assertThat(result.getGroup()).isEqualTo(testItems.getGroup2());
        });

        verify(studentRepository).findById(testData.getStudentId());
        verify(groupService).getById(testItems.getGroup2().getId());
    }

    @Test
    void getById_shouldReturnStudentInstance_whenIsFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getStudent()));

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
    void saveUser_shouldReturnStudentInstanceWithId_whenIsSavedSuccessfully() {
        when(studentRepository.save(any(Student.class))).thenReturn((Student) testItems.getUser());

        assertThat(studentService.saveUser(testItems.userRegistrationForm()).getId()).isNotNull();

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void getLessonsForDay_shouldReturnListOfLessonsPerSpecifyDay_whenIsInvoke() {
        when(lessonRepository.getLessonByGroupAndDate(any(Group.class), any(LocalDate.class))).thenReturn(testItems.getAllLessonsOfGroupPerDay());

        assertThat(studentService.getLessonsForDay(testItems.getStudent(), testItems.getDateOfMonday())).hasSize(8);

        verify(lessonRepository).getLessonByGroupAndDate(testItems.getGroup(), testItems.getDateOfMonday());
    }

    @Test
    void getLessonsForWeek_shouldReturnListOfLessonsPerSpecifyWeek_whenIsInvoke() {
        when(lessonRepository.findByGroupAndDateBetween(any(Group.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.getAllLessonsOfGroupPerWeek());

        assertThat(studentService.getLessonsForWeek(testItems.getStudent(), testItems.getDateOfMonday(), testItems.getDateOfFriday())).hasSize(30);

        verify(lessonRepository).findByGroupAndDateBetween(testItems.getGroup(), testItems.getDateOfMonday(), testItems.getDateOfFriday());
    }

    @Test
    void getAll_shouldReturnListOfStudent_whenIsInvoke() {
        when(studentRepository.findAll()).thenReturn(testItems.getStudents());

        assertThat(studentService.getAll()).hasSize(5);

        verify(studentRepository).findAll();
    }


    @Test
    void getUserNames() {
        when(studentRepository.getUserNamesById(anyLong())).thenReturn(testItems.getUserNames());

        studentService.getUserNames(testData.getStudentId());

        verify(studentRepository).getUserNamesById(testData.getStudentId());
    }

    @Test
    void updateNames() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItems.getStudent()));
        when(studentRepository.save(any(Student.class))).thenReturn((Student) testItems.getUserWithUpdatedNames());

        studentService.updateNames(testItems.getUserNames());

        verify(studentRepository).findById(testData.getStudentId());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void getUserEmail() {
        when(studentRepository.getUserEmailById(anyLong())).thenReturn(testItems.getEmailDto());

        studentService.getUserEmail(testData.getStudentId());

        verify(studentRepository).getUserEmailById(testData.getStudentId());
    }

    @Test
    void updateEmail() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItems.getStudent()));
        when(studentRepository.save(any(Student.class))).thenReturn(testItems.studentWithEmail());

        studentService.updateEmail(testItems.getEmailDto());

        verify(studentRepository).findById(testData.getStudentId());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void getDetails() {
        when(studentRepository.getUserDataById(testData.getStudentId())).thenReturn(testItems.getUserDto());

        studentService.getDetails(testData.getStudentId());

        verify(studentRepository).getUserDataById(testData.getStudentId());
    }

    @Test
    void updatePassword() {
        when(studentRepository.findById(testData.getStudentId())).thenReturn(Optional.ofNullable(testItems.getStudent()));
        when(passwordEncoder.encode(anyString())).thenReturn(testItems.getEncodedPassword());
        when(studentRepository.save(any(Student.class))).thenReturn((Student) testItems.studentWithPassword());

        studentService.updatePassword(testItems.getChangePasswordForm());

        verify(studentRepository).findById(testData.getStudentId());
        verify(passwordEncoder).encode(testItems.getPassword());
        verify(studentRepository).save(any(Student.class));
    }

    @Test()
    void deleteUser_shouldThrowEntityNotFoundException_whenStudentIsNotFound() {
        when(studentRepository.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> studentService.deleteUser(testData.getStudentId()));

        verify(studentRepository).findById(testData.getStudentId());
    }


    @Test
    void deleteUser_shouldRemoveStudent_whenIsFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(testItems.getStudent()));

        studentService.deleteUser(testData.getStudentId());

        verify(studentRepository).findById(testData.getStudentId());
        verify(studentRepository).delete(testItems.getStudent());
    }
}