package ua.foxminded.WebProject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.persistence.entity.Staff;
import ua.foxminded.WebProject.service.StaffService;

@Controller
@RequestMapping("/staff")
@PreAuthorize("hasAnyRole('staff', 'admin')")
public class StaffController extends AbstractUserCrudController<Staff> {

    public StaffController(StaffService staffService, BCryptPasswordEncoder passwordEncoder) {
        super(staffService, passwordEncoder, Staff.class);
    }
}
