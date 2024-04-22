package ua.foxminded.WebProject.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Secured("admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping
    public String adminPage() {
        return "admin/admin";
    }
}


