package ua.foxminded.WebProject.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.foxminded.WebProject.util.FieldsValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = FieldsValidator.class)
public @interface FieldsCompare {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String first();
    String second();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsCompare[] value();
    }
}