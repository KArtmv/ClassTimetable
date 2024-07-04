package ua.foxminded.WebProject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.persistence.entity.Admin;
import ua.foxminded.WebProject.service.AdminService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('admin', 'staff')")
public class AdminController extends AbstractUserCrudController<Admin> {

    public AdminController(AdminService adminService, BCryptPasswordEncoder passwordEncoder) {
        super(adminService, passwordEncoder, Admin.class);
    }
}


