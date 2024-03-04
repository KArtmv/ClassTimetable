package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.persistence.entity.Course;
import ua.foxminded.WebProject.persistence.entity.Group;

import java.util.List;

public interface GroupService extends AbstractService<Group, Long> {
    List<Group> findAll();
}
