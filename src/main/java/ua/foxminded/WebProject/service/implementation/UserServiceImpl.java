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
import ua.foxminded.WebProject.persistence.repository.AbstractUserRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class UserServiceImpl<T extends User> {

    protected final AbstractUserRepository<T, Long> repository;
    protected final BCryptPasswordEncoder passwordEncoder;

    public UserNames getUserNames(Long id) {
        return repository.getUserNamesById(id);
    }

    @Transactional
    public T updateNames(UserNames userNameDto) {
        T user = getById(userNameDto.id());
        user.setFirstName(userNameDto.firstName());
        user.setLastName(userNameDto.lastName());
        return repository.save(user);
    }

    public UserEmailDto getUserEmail(Long id) {
        return repository.getUserEmailById(id);
    }

    @Transactional
    public T updateEmail(UserEmailDto emailDto) {
        T user = getById(emailDto.id());
        user.setEmail(emailDto.email());
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
    public T saveUser(T user) {
        return repository.save(user);
    }

    public T getById(Long id){
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<T> getAll(){
        return repository.findAll();
    }
}
