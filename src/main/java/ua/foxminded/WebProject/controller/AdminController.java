package ua.foxminded.WebProject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping
    public String adminPage() {
        return "admin/admin";
    }
}


