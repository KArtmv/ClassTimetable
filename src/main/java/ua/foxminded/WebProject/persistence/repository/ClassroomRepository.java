package ua.foxminded.WebProject.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.WebProject.persistence.entity.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}