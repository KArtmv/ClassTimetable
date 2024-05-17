package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.GroupDto;
import ua.foxminded.WebProject.persistence.entity.Group;

public interface GroupService extends AbstractService<Group, Long> {
    Group saveGroup(GroupDto group);

    void remove(Group group);
}
