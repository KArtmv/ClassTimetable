package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.repository.GroupRepository;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GroupServiceImpl.class)
class GroupServiceImplTest {
    private final TestItems testItems = new TestItems();
    private final TestData testData = new TestData();
    @MockBean
    private GroupRepository repository;
    @Autowired
    private GroupServiceImpl service;

    @Test
    void findAll_shouldReturnListOfGroups_whenIsInvoke() {
        when(repository.findAll()).thenReturn(testItems.getGroups());

        assertThat(service.getAll()).hasSize(3);

        verify(repository).findAll();
    }

    @Test
    void saveGroup_shouldReturnGroupInstanceWithId_whenIsSavedSuccessfully() {
        when(repository.save(any(Group.class))).thenReturn(testItems.getGroup());

        assertThat(service.saveGroup(testData.getGroupDto()).getId()).isNotNull();

        verify(repository).save(any(Group.class));
        verify(repository).findAll();
    }

    @Test
    void saveGroup_shouldThrowsDataIntegrityViolationException_whenSavingDuplicate() {
        when(repository.save(any(Group.class))).thenThrow(DataIntegrityViolationException.class);

        assertThat(service.saveGroup(testData.getGroupDto()).getId()).isNull();

        verify(repository).save(any(Group.class));
        verify(repository).findAll();
    }

    @Test
    void saveGroup_shouldReturnEmptyGroupInstance_whenSavingDuplicate() {
        when(repository.findAll()).thenReturn(testItems.getGroups());

        assertThat(service.saveGroup(testData.getGroupDto()).getId()).isNull();

        verify(repository).findAll();
    }

    @Test
    void remove_shouldRemoveGroupInstance_whenIsRemovedSuccessfully() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getGroup()));

        service.remove(testItems.getGroup());

        verify(repository).findById(testData.getCourseId());
    }

    @Test
    void getById_shouldReturnGroupInstanceWithId_whenIsFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(testItems.getGroup()));

        Group result = service.getById(testData.getGroupId());
        assertAll(() -> {
            assertThat(result).isNotNull();
            assertThat(result.getGroupName()).isEqualTo(testData.getGroupName());
        });
        verify(repository).findById(testData.getGroupId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenIdIsNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(testData.getGroupId()));

        verify(repository).findById(testData.getGroupId());
    }
}