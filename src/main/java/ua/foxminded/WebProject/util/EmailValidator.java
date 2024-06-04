package ua.foxminded.WebProject.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.foxminded.WebProject.annotations.UniqueEmail;
import ua.foxminded.WebProject.persistence.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class EmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findUserByEmail(string).isEmpty();
    }
}
