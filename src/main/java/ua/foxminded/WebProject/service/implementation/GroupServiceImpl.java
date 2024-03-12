package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.repository.GroupRepository;
import ua.foxminded.WebProject.service.GroupService;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GroupServiceImpl implements GroupService {

    private GroupRepository repository;

    @Override
    public List<Group> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Group saveGroup(Group group) {
        return repository.save(group);
    }

    @Override
    public Group getById(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found course by given id:" + id));
    }
}
