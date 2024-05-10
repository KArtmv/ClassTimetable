package ua.foxminded.WebProject.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class CustomErrorController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        return modelAndView;
    }

    @RequestMapping("/error")
    public ModelAndView handleError() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error");
        return mav;
    }
}
