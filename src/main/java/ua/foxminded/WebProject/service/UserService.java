package ua.foxminded.WebProject.service;

import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.DTO.UserEmailDto;
import ua.foxminded.WebProject.DTO.UserNames;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.form.UserRegistrationForm;

public interface UserService {
    UserNames getUserNames(Long id);
    User updateNames(UserNames userNameDto);

    UserEmailDto getUserEmail(Long id);
    User updateEmail(UserEmailDto emailDto);

    UserDto getDetails(Long id);

    User updatePassword(ChangePasswordForm changePasswordForm);

    User saveUser(UserRegistrationForm registrationForm);

    void deleteUser(Long id);
}
