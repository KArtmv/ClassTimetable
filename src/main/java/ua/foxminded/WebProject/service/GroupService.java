package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.persistence.entity.Group;

public interface GroupService extends AbstractService<Group, Long> {
    Group saveGroup(Group group);
}
