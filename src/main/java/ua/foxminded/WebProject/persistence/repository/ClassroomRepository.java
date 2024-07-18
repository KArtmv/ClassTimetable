package ua.foxminded.WebProject.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.WebProject.DTO.ClassroomDto;
import ua.foxminded.WebProject.persistence.entity.Classroom;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    @Query("select new ua.foxminded.WebProject.DTO.ClassroomDto(c.id, c.classroomName) from #{#entityName} c")
    List<ClassroomDto> getAllClassroomsNames();
}