package ua.foxminded.WebProject.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.form.UserRegistrationForm;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.persistence.repository.UserCrudRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class AbstractUserService<T extends User> {

    protected final UserCrudRepository<T, Long> repository;
    protected final BCryptPasswordEncoder passwordEncoder;
    private final Class<T> clazz;
    private final UserRepository userRepository;

    @Transactional
    public T updateNames(UserDto userDto) {
        T user = getById(userDto.id());
        user.setFirstName(toCapitalize(userDto.firstName()));
        user.setLastName(toCapitalize(userDto.lastName()));
        return repository.save(user);
    }

    @Transactional
    public T updateEmail(UserDto emailDto) {
        T user = getById(emailDto.id());
        user.setEmail(emailDto.email().toLowerCase());
        return repository.save(user);
    }

    public UserDto getDetails(Long id) {
        return repository.getUserDataById(id);
    }

    @Transactional
    public T updatePassword(ChangePasswordForm changePasswordForm) {
        T user = getById(changePasswordForm.getId());
        user.setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
        return repository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        repository.delete(getById(id));
    }


    @Transactional
    public T saveUser(UserRegistrationForm form) {
        try {
            T user = getNewInstance();
            user.setFirstName(toCapitalize(form.getFirstName()));
            user.setLastName(toCapitalize(form.getLastName()));
            user.setEmail(form.getEmail().toLowerCase());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
            return repository.save(user);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException("Failed to create a new instance of user", e);
        }
    }

    public T getById(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    private T getNewInstance() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.getDeclaredConstructor().newInstance();
    }

    private String toCapitalize(String str) {
        return StringUtils.capitalize(str.toLowerCase().trim());
    }
}
