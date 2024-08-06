package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.persistence.entity.Student;

import java.util.List;

public interface StudentService extends UserService<Student>, AbstractService<Student, Long> {
    StudentGroupDto getStudentGroup(Long id);

    Student updateStudentGroup(StudentGroupDto studentGroupDto);

    List<UserDto> getStudentsByGroupId(Long groupId);
}
