package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.foxminded.WebProject.persistence.entity.Classroom;
import ua.foxminded.WebProject.persistence.repository.ClassroomRepository;
import ua.foxminded.WebProject.util.TestItems;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClassroomServiceImplTest {

    private final TestItems testItems = new TestItems();
    @Mock
    private ClassroomRepository repository;
    @InjectMocks
    private ClassroomServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnListOfClassroom_whenIsInvoke() {
        when(repository.findAll()).thenReturn(testItems.getClassrooms());

        assertThat(service.findAll()).hasSize(4);

        verify(repository).findAll();
    }

    @Test
    void getById_shouldReturnClassroomInstance_whenIsFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getClassroom()));

        Classroom result = service.getById(testItems.getClassroomId());

        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.getClassroomName()).isEqualTo(testItems.getClassroomName());
        });

        verify(repository).findById(testItems.getClassroomId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(testItems.getClassroomId()));

        verify(repository).findById(testItems.getClassroomId());
    }
}