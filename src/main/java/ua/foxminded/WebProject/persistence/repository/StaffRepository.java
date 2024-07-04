package ua.foxminded.WebProject.persistence.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.WebProject.persistence.entity.Staff;

@Repository
public interface StaffRepository extends UserCrudRepository<Staff, Long> {
}