package ua.foxminded.WebProject.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MyLocalDate {

    public LocalDate getCurrentDate(){
        return LocalDate.now();
    }
}
