package ua.foxminded.WebProject.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.foxminded.WebProject.annotations.FieldsCompare;

@Getter
@Setter
@NoArgsConstructor
@FieldsCompare(first = "password", second = "confirmPassword", message = "Oops! Looks like your passwords don't match. Double-check and try again.")
public class ChangePasswordForm {

    private Long id;

    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters.")
    @NotEmpty(message = "Old password is required.")
    private String oldPassword;

    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters.")
    @NotEmpty(message = "Password is required.")
    private String password;

    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters.")
    @NotEmpty(message = "Confirm password is required.")
    private String confirmPassword;
}
