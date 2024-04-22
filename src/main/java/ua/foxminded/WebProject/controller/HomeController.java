package ua.foxminded.WebProject.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
        model.addAttribute("currentDate", localDate.getCurrentDate());
        return "home";
    }
}
