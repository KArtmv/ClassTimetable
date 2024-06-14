package ua.foxminded.WebProject.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.WebProject.util.AlphabetValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = AlphabetValidator.class)
public @interface AlphabeticChars {

    String message() default "To enter only available alphabetic characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
