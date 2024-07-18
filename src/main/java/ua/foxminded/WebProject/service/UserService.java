package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.NamesDto;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.form.UserRegistrationForm;
import ua.foxminded.WebProject.persistence.entity.User;

import java.util.List;

public interface UserService<T extends User> {
    T updateNames(UserDto userNameDto);

    T updateEmail(UserDto emailDto);

    UserDto getDetails(Long id);

    T updatePassword(ChangePasswordForm changePasswordForm);

    T saveUser(UserRegistrationForm user);

    void deleteUser(Long id);

    User getUserByEmail(String email);

    List<T> getAll();

    List<NamesDto> getAllNames();
}
