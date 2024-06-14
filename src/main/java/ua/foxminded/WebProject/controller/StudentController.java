package ua.foxminded.WebProject.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.DTO.UserEmailDto;
import ua.foxminded.WebProject.DTO.UserNames;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.form.UserRegistrationForm;
import ua.foxminded.WebProject.persistence.entity.Student;
import ua.foxminded.WebProject.persistence.entity.User;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.service.GroupService;
import ua.foxminded.WebProject.service.StudentService;

import java.security.Principal;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('admin', 'student', 'staff')")
public class StudentController {

    private static final String USER = "student";

    private final StudentService studentService;
    private final GroupService groupService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public String studentPage(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "student/student";
    }

    @GetMapping("/addStudent")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public String addStudent(Model model) {
        model.addAttribute("registrationForm", new UserRegistrationForm());
        model.addAttribute("user", USER);
        model.addAttribute("path", USER);
        model.addAttribute("groups", groupService.getAll());
        return "user/addUser";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    @Transactional
    public String saveStudent(@Valid @ModelAttribute("registrationForm") UserRegistrationForm user,
                              BindingResult result,
                              @RequestParam("groupId") Long groupId,
                              Model model) {
        if (result.hasErrors() || groupId == 0) {
            model.addAttribute("selectedGroupId", groupId);
            model.addAttribute("user", USER);
            model.addAttribute("path", USER);
            model.addAttribute("groups", groupService.getAll());
            model.addAttribute("emptyGroup", groupId == 0);
            return "user/addUser";
        }

        Student student = studentService.saveUser(user);
        student.setGroup(groupService.getById(groupId));

        return "redirect:/student";
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public String viewStudentDetails(@PathVariable Long id, Model model) {
        UserDto userDto = studentService.getDetails(id);
        model.addAttribute("user", USER);
        model.addAttribute("path", USER);
        model.addAttribute("userDto", userDto);
        model.addAttribute("studentGroupDto", studentService.getStudentGroup(userDto.id()));

        return "user/userDetails";
    }

    @GetMapping("/edit/names/{id}")
    public String editStudentName(@PathVariable Long id, Model model) {
        model.addAttribute("userNames", studentService.getUserNames(id));
        model.addAttribute("path", USER);
        return "user/editUserNames";
    }

    @PostMapping("/update/names")
    public String updateStudentNames(@Valid UserNames userNames, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("path", USER);
            return "user/editUserNames";
        }
        studentService.updateNames(userNames);
        return "redirect:/student";
    }

    @GetMapping("/edit/email/{id}")
    public String editStudentEmail(@PathVariable Long id, Model model) {
        model.addAttribute("path", USER);
        model.addAttribute("userEmailDto", studentService.getUserEmail(id));
        return "user/editUserEmail";
    }

    @PostMapping("/update/email")
    public String updateEmail(@Valid UserEmailDto userEmail, BindingResult result, Model model) {
        if (userEmail.email().equals(studentService.getUserEmail(userEmail.id()).email())) {
            result.rejectValue("email", "", "Given email is current user email.");
        }

        if (result.hasErrors()) {
            model.addAttribute("path", USER);
            return "user/editUserEmail";
        }

        studentService.updateEmail(userEmail);
        return "redirect:/student";
    }

    @GetMapping("/edit/group/{id}")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public String editStudentGroup(@PathVariable Long id, Model model) {
        model.addAttribute("studentGroupDto", studentService.getStudentGroup(id));
        model.addAttribute("groups", groupService.getAll());
        return "student/feature/editGroup";
    }

    @PostMapping("/update/group")
    @PreAuthorize("hasAnyRole('admin', 'staff')")
    public String updateStudentGroup(StudentGroupDto studentGroupDto) {
        studentService.updateStudentGroup(studentGroupDto);
        return "redirect:/student";
    }

    @GetMapping("/edit/password")
    @PreAuthorize("hasRole('student')")
    public String editStudentPassword(Model model) {
        model.addAttribute("path", USER);
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "user/editPassword";
    }

    @PostMapping("/update/password")
    @PreAuthorize("hasRole('student')")
    public String updateStudentPassword(@Valid ChangePasswordForm changePasswordForm, BindingResult result, Model model, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow(EntityNotFoundException::new);

        if (!passwordEncoder.matches(changePasswordForm.getOldPassword(), user.getPassword())) {
            result.rejectValue("oldPassword", "", "Old password is incorrect.");
        }

        if (result.hasErrors()) {
            model.addAttribute("path", USER);
            return "user/editPassword";
        }
        changePasswordForm.setId(user.getId());
        studentService.updatePassword(changePasswordForm);
        return "redirect:/student/details?success=true";
    }

    @GetMapping("/details")
    @PreAuthorize("hasRole('student')")
    public String viewMyDetails(Model model, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("user", USER);
        model.addAttribute("path", USER);
        model.addAttribute("userDto", studentService.getDetails(user.getId()));
        return "user/userDetails";
    }

    @GetMapping("/remove/{id}")
    @PreAuthorize("hasRole('admin')")
    public String deleteForm(@PathVariable Long id, Model model) {
        model.addAttribute("path", USER);
        model.addAttribute("userNames", studentService.getUserNames(id));
        return "user/deleteUser";
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('admin')")
    public String delete(@RequestParam("userId") Long userId) {
        studentService.deleteUser(userId);
        return "redirect:/student";
    }
}
