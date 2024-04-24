package ua.foxminded.WebProject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.persistence.repository.StaffRepository;

@Controller
@RequestMapping("/staff")
@PreAuthorize("hasAnyRole('staff', 'admin')")
@RequiredArgsConstructor
public class StaffController {

    private final StaffRepository staffRepository;

    @GetMapping
    public String staffPage(Model model) {
        model.addAttribute("staffs", staffRepository.findAll());
        return "staff/staff";
    }
}
