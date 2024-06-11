package ua.foxminded.WebProject.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ua.foxminded.WebProject.annotations.AlphabeticChars;

import java.util.regex.Pattern;

public class AlphabetValidator implements ConstraintValidator<AlphabeticChars, String> {

    private final Pattern pattern = Pattern.compile("[a-zA-Z]");

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        return string != null && pattern.matcher(string).matches();
    }
}
