package ua.foxminded.WebProject.persistence.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.WebProject.persistence.entity.Admin;

@Repository
public interface AdminRepository extends UserCrudRepository<Admin, Long> {
}