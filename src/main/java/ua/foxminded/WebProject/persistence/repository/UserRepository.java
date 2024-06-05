package ua.foxminded.WebProject.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.DTO.UserEmailDto;
import ua.foxminded.WebProject.DTO.UserNames;
import ua.foxminded.WebProject.persistence.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select new ua.foxminded.WebProject.DTO.UserNames(u.id, u.firstName, u.lastName) from User u where u.id = ?1")
    UserNames getUserNamesById(Long id);

    @Query("select new ua.foxminded.WebProject.DTO.UserEmailDto(u.id, u.email) from  User u where u.id = ?1")
    UserEmailDto getUserEmailById(Long id);

    @Query("select new ua.foxminded.WebProject.DTO.UserDto(u.id, u.firstName, u.lastName, u.email) from User u where u.id = ?1")
    UserDto getUserDataById(Long id);

    Optional<User> findUserByEmail(String email);
}