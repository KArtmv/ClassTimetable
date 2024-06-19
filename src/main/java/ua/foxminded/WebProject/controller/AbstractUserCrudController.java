package ua.foxminded.WebProject.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.form.UserRegistrationForm;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.service.UserService;

import java.security.Principal;

public abstract class AbstractUserCrudController<T extends User> {

    protected final String user;
    protected final UserService<T> userService;
    private final BCryptPasswordEncoder passwordEncoder;

    protected AbstractUserCrudController(UserService<T> userService, BCryptPasswordEncoder passwordEncoder, Class<T> clazz) {
        this.user = clazz.getSimpleName().toLowerCase();
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String userPage(Model model) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("path", this.user);
        model.addAttribute("user", this.user);
        return "user/user";
    }

    @GetMapping("/addUser")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public String addUser(Model model) {
        model.addAttribute("registrationForm", new UserRegistrationForm());
        model.addAttribute("user", this.user);
        model.addAttribute("path", this.user);
        return "user/addUser";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public String saveUser(@Valid @ModelAttribute("registrationForm") UserRegistrationForm user,
                           BindingResult result,
                           @RequestParam(required = false) Long groupId,                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", this.user);
            model.addAttribute("path", this.user);
            return "user/addUser";
        }
        userService.saveUser(user);
        return "redirect:/" + this.user;
    }

    @GetMapping("/details")
    protected String viewMyDetails(Model model, Principal principal) {
        User loggedUser = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", this.user);
        model.addAttribute("path", this.user);
        model.addAttribute("userDto", userService.getDetails(loggedUser.getId()));
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "user/userDetails";
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public String viewUserDetails(@PathVariable Long id, Model model) {
        model.addAttribute("user", this.user);
        model.addAttribute("path", this.user);
        model.addAttribute("userDto", userService.getDetails(id));

        return "user/userDetails";
    }

    @PostMapping("/update-names")
    public String updateUserNames(@Valid UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", this.user);
            model.addAttribute("path", this.user);
            model.addAttribute("nameFormOpen", true);
            model.addAttribute("changePasswordForm", new ChangePasswordForm());
            return "user/userDetails";
        }
        userService.updateNames(userDto);
        return "redirect:/student";
    }

    @PostMapping("/update-email")
    public String updateEmail(@Valid UserDto userDto, BindingResult result, Model model) {
        if (userDto.email().equals(userService.getDetails(userDto.id()).email())) {
            result.rejectValue("email", "", "Given email is current user email.");
        }

        if (result.hasErrors()) {
            model.addAttribute("path", this.user);
            model.addAttribute("emailFormOpen", true);
            model.addAttribute("changePasswordForm", new ChangePasswordForm());
            return "user/userDetails";
        }

        userService.updateEmail(userDto);
        return "redirect:/" + this.user;
    }

    @PostMapping("/update-password")
    public String updateUserPassword(@Valid ChangePasswordForm changePasswordForm, BindingResult result, Model model, Principal principal) {
        User loggedUser = userService.getUserByEmail(principal.getName());

        if (!passwordEncoder.matches(changePasswordForm.getOldPassword(), loggedUser.getPassword())) {
            result.rejectValue("oldPassword", "", "Old password is incorrect.");
        }

        if (result.hasErrors()) {
            model.addAttribute("path", this.user);
            model.addAttribute("passwordFormOpen", true);
            model.addAttribute("userDto", userService.getDetails(loggedUser.getId()));
            model.addAttribute("changePasswordForm", new ChangePasswordForm());
            return "user/userDetails";
        }
        changePasswordForm.setId(loggedUser.getId());
        userService.updatePassword(changePasswordForm);
        return "redirect:/" + this.user + "/details?success=true";
    }

    @GetMapping("/remove/{id}")
    @PreAuthorize("hasRole('admin')")
    public String deleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("path", user);
        model.addAttribute("userDto", userService.getDetails(id));
        return "user/deleteUser";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/" + this.user;
    }
}
