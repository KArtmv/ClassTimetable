package ua.foxminded.WebProject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.persistence.entity.Admin;
import ua.foxminded.WebProject.service.AdminService;
import ua.foxminded.WebProject.service.UserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('admin', 'staff')")
public class AdminController extends AbstractUserCrudController<Admin> {

    public AdminController(AdminService adminService, BCryptPasswordEncoder passwordEncoder) {
        super(adminService, passwordEncoder, Admin.class);
    }
}


