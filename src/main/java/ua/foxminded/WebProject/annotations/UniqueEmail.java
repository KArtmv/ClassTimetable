package ua.foxminded.WebProject.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.WebProject.util.EmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = EmailValidator.class)
public @interface UniqueEmail {

    String message() default "There is already an account registered with that email.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
