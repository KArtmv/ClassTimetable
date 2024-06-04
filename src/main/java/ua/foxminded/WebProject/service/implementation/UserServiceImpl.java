package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.DTO.UserEmailDto;
import ua.foxminded.WebProject.DTO.UserNames;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.persistence.repository.UserRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class UserServiceImpl {

    protected final UserRepository userRepository;
    protected final BCryptPasswordEncoder passwordEncoder;

    public UserNames getUserNames(Long id) {
        return userRepository.getUserNamesById(id);
    }

    @Transactional
    public User updateNames(UserNames userNameDto) {
        User user = getUser(userNameDto.id());
        user.setFirstName(userNameDto.firstName());
        user.setLastName(userNameDto.lastName());
        return userRepository.save(user);
    }

    public UserEmailDto getUserEmail(Long id) {
        return userRepository.getUserEmailById(id);
    }

    @Transactional
    public User updateEmail(UserEmailDto emailDto) {
        User user = getUser(emailDto.id());
        user.setEmail(emailDto.email());
        return userRepository.save(user);
    }

    public UserDto getDetails(Long id) {
        User user = getUser(id);
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail()).build();
    }

    @Transactional
    public User updatePassword(ChangePasswordForm changePasswordForm) {
        User user = getUser(changePasswordForm.getId());
        user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(getUser(id));
    }

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
