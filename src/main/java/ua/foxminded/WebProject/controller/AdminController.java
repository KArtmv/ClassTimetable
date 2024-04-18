package ua.foxminded.WebProject.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Secured("admin")
@AllArgsConstructor
public class AdminController {

    @GetMapping
    public String adminPage() {
        return "admin/admin";
    }
}


