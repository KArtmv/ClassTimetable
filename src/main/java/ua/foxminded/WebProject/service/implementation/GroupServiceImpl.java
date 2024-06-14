package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.GroupDto;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.repository.GroupRepository;
import ua.foxminded.WebProject.service.GroupService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repository;

    @Override
    public Group getById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found course by given id:" + id));
    }

    @Override
    public List<Group> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void remove(Group group) {
        repository.delete(getById(group.getId()));
    }

    @Override
    @Transactional(rollbackFor = DataIntegrityViolationException.class)
    public Group saveGroup(GroupDto groupDto) {
        try {
            Group group = toGroup(groupDto);
            return checkDuplicate(group) ? new Group() : repository.save(group);
        } catch (DataIntegrityViolationException e) {
            return new Group();
        }
    }

    private boolean checkDuplicate(Group group) {
        return getAll().stream().anyMatch(g -> g.getGroupName().equals(group.getGroupName()));
    }

    private Group toGroup(GroupDto groupDto) {
        return new Group(groupDto.groupName());
    }


}
