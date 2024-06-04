package ua.foxminded.WebProject.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ua.foxminded.WebProject.annotations.FieldsCompare;

import java.lang.reflect.Field;

public class FieldsValidator implements ConstraintValidator<FieldsCompare, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldsCompare constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field firstField = o.getClass().getDeclaredField(firstFieldName);
            Field secondField = o.getClass().getDeclaredField(secondFieldName);

            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object firstValue = firstField.get(o);
            Object secondValue = secondField.get(o);

            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(firstFieldName).addConstraintViolation();

            return firstValue.equals(secondValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }

    }
}

//    @Override
//    public boolean isValid(RegistrationForm registrationForm, ConstraintValidatorContext constraintValidatorContext) {
//        constraintValidatorContext.disableDefaultConstraintViolation();
//        constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
//                .addPropertyNode("password").addConstraintViolation();
//        return registrationForm.getPassword().equals(registrationForm.getConfirmPassword());
//    }
//}

//public class PasswordValidator implements ConstraintValidator<PasswordCompare, RegistrationForm> {
//
//    @Override
//    public boolean isValid(RegistrationForm registrationForm, ConstraintValidatorContext constraintValidatorContext) {
//        constraintValidatorContext.disableDefaultConstraintViolation();
//        constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
//                .addPropertyNode("password").addConstraintViolation();
//        return registrationForm.getPassword().equals(registrationForm.getConfirmPassword());
//    }
//}
