package ua.foxminded.WebProject.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.form.UserRegistrationForm;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.StudentService;

import java.security.Principal;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasAnyRole('admin', 'student', 'staff')")
public class StudentController extends AbstractUserCrudController<Student> {

    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(BCryptPasswordEncoder passwordEncoder, StudentService studentService, GroupService groupService) {
        super(studentService, passwordEncoder, Student.class);
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @Override
    public String addUser(Model model) {
        model.addAttribute("registrationForm", new UserRegistrationForm());
        model.addAttribute("path", user);
        model.addAttribute("groups", groupService.getAll());

        return "user/addUser";
    }

    @Override
    @Transactional
    public String saveUser(@Valid @ModelAttribute("registrationForm") UserRegistrationForm registrationForm,
                           BindingResult result,
                           @RequestParam("groupId") Long groupId,
                           Model model) {
        if (result.hasErrors() || groupId == 0) {
            model.addAttribute("selectedGroupId", groupId);
            model.addAttribute("path", user);
            model.addAttribute("groups", groupService.getAll());
            model.addAttribute("emptyGroup", groupId == 0);

            return "user/addUser";
        }

        Student student = studentService.saveUser(registrationForm);
        student.setGroup(groupService.getById(groupId));

        return "redirect:/student";

    }

    @Override
    protected String viewMyDetails(Model model, Principal principal) {
        User loggedUser = userService.getUserByEmail(principal.getName());
        super.setPathWithUserDto(model, userService.getDetails(loggedUser.getId()));
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        model.addAttribute("studentGroupDto", studentService.getStudentGroup(loggedUser.getId()));

        return "user/userDetails";
    }

    @Override
    public String viewUserDetails(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getDetails(id);
        super.setPathWithUserDto(model, userDto);
        model.addAttribute("studentGroupDto", studentService.getStudentGroup(userDto.id()));

        return "user/userDetails";
    }

    @Override
    public String updateUserNames(@Valid UserDto userDto, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            super.setPathWithUserDto(model, userDto);
            model.addAttribute("nameFormOpen", true);
            model.addAttribute("studentGroupDto", studentService.getStudentGroup(userDto.id()));
            if (principal.getName().equalsIgnoreCase(userDto.email())) {
                model.addAttribute("changePasswordForm", new ChangePasswordForm());
            }
            return "user/userDetails";
        }
        userService.updateNames(userDto);
        return "redirect:/student";
    }

    @Override
    public String updateEmail(@Valid UserDto userDto, BindingResult result, Model model, Principal principal) {
        if (userDto.email().equals(userService.getDetails(userDto.id()).email())) {
            result.rejectValue("email", "", "Given email is current user email.");
        }

        if (result.hasErrors()) {
            super.setPathWithUserDto(model, userDto);
            model.addAttribute("emailFormOpen", true);
            model.addAttribute("studentGroupDto", studentService.getStudentGroup(userDto.id()));
            if (principal.getName().equalsIgnoreCase(userDto.email())) {
                model.addAttribute("changePasswordForm", new ChangePasswordForm());
            }
            return "user/userDetails";
        }

        userService.updateEmail(userDto);
        return "redirect:/student";
    }

    @Override
    public String updateUserPassword(@Valid ChangePasswordForm changePasswordForm, BindingResult result, Model model, Principal principal) {
        User loggedUser = userService.getUserByEmail(principal.getName());

        if (!super.passwordEncoder.matches(changePasswordForm.getOldPassword(), loggedUser.getPassword())) {
            result.rejectValue("oldPassword", "", "Old password is incorrect.");
        }

        if (result.hasErrors()) {
            model.addAttribute("passwordFormOpen", true);
            super.setPathWithUserDto(model, studentService.getDetails(loggedUser.getId()));
            model.addAttribute("studentGroupDto", studentService.getStudentGroup(loggedUser.getId()));
            return "user/userDetails";
        }
        changePasswordForm.setId(loggedUser.getId());
        userService.updatePassword(changePasswordForm);
        return "redirect:/student/details?success=true";
    }

    @GetMapping("/edit-group/{id}")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public String editStudentGroup(@PathVariable Long id, Model model) {
        model.addAttribute("studentGroupDto", studentService.getStudentGroup(id));
        model.addAttribute("groups", groupService.getAll());

        return "student/feature/editGroup";
    }

    @PostMapping("/update-group")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public String updateStudentGroup(StudentGroupDto studentGroupDto) {
        studentService.updateStudentGroup(studentGroupDto);

        return "redirect:/student";
    }
}
