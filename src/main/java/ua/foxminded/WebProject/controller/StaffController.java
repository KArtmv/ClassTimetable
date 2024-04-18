package ua.foxminded.WebProject.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
@Secured({"staff", "admin"})
public class StaffController {

    @GetMapping
    public String staff(){
        return "staff/staff";
    }
}
