package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.DTO.UserEmailDto;
import ua.foxminded.WebProject.DTO.UserNames;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.form.UserRegistrationForm;
import ua.foxminded.WebProject.persistence.entity.User;

public interface UserService<T extends User> {
    UserNames getUserNames(Long id);

    T updateNames(UserNames userNameDto);

    UserEmailDto getUserEmail(Long id);

    T updateEmail(UserEmailDto emailDto);

    UserDto getDetails(Long id);

    T updatePassword(ChangePasswordForm changePasswordForm);

    T saveUser(UserRegistrationForm user);

    void deleteUser(Long id);
}
