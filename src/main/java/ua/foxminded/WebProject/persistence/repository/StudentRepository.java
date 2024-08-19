package ua.foxminded.WebProject.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.persistence.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends UserCrudRepository<Student, Long> {

    @Query("select new ua.foxminded.WebProject.DTO.StudentGroupDto(s.id, s.group.id, s.group.groupName) from Student s where s.id = ?1")
    StudentGroupDto getStudentGroup(Long id);

    List<UserDto> getAllByGroupId(Long groupId);
}