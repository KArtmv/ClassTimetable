package ua.foxminded.WebProject.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.foxminded.WebProject.annotations.FieldsCompare;
import ua.foxminded.WebProject.annotations.UniqueEmail;

@Getter
@Setter
@NoArgsConstructor
@FieldsCompare(first = "password", second = "confirmPassword", message = "Oops! Looks like your passwords don't match. Double-check and try again.")
public class UserRegistrationForm {

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 15, message = "First name must be between 2 and 15 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 20, message = "Last name must be between 2 and 20 characters.")
    private String lastName;

    @Email(message = "Please enter a valid email address.")
    @NotEmpty(message = "Email address is required.")
    @UniqueEmail
    private String email;

    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters.")
    @NotEmpty(message = "Password is required.")
    private String password;

    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters.")
    @NotEmpty(message = "Confirm password is required.")
    private String confirmPassword;
}

