package ua.foxminded.WebProject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.foxminded.WebProject.util.MyLocalDate;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MyLocalDate localDate;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        role = role.substring(1, role.length() - 1);
        switch (role) {
            case "ROLE_admin" -> {
                return "redirect:/admin";
            }
            case "ROLE_teacher" -> {
                return "redirect:/teacher";
            }
            case "ROLE_student" -> {
                return "redirect:/student";
            }
            case "ROLE_staff" -> {
                return "redirect:/staff";
            }
            default -> {
                model.addAttribute("currentDate", localDate.getCurrentDate());
                return "home";
            }
        }
    }
}
