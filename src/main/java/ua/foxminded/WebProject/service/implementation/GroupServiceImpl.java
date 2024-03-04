package ua.foxminded.WebProject.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.foxminded.WebProject.exception.InvalidIdException;
import ua.foxminded.WebProject.persistence.entity.Group;
import ua.foxminded.WebProject.persistence.repository.GroupRepository;
import ua.foxminded.WebProject.service.GroupService;

import java.util.List;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private GroupRepository repository;

    @Override
    public List<Group> findAll() {
        return repository.findAll();
    }

    @Override
    public Group getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new InvalidIdException("Not found given id:" + id));
    }
}
