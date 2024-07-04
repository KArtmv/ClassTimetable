package ua.foxminded.WebProject.persistence.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.WebProject.persistence.entity.Teacher;

@Repository
public interface TeacherRepository extends UserCrudRepository<Teacher, Long> {
}