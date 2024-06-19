package ua.foxminded.WebProject.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.persistence.entity.User;

import java.io.Serializable;

@NoRepositoryBean
public interface UserCrudRepository<T extends User, ID extends Serializable> extends JpaRepository<T, ID> {

    @Query("select new ua.foxminded.WebProject.DTO.UserDto(u.id, u.firstName, u.lastName, u.email) from #{#entityName} u where u.id = ?1")
    UserDto getUserDataById(ID id);
}
