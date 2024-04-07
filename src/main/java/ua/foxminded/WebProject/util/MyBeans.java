package ua.foxminded.WebProject.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.random.RandomGenerator;

@Component
public class MyBeans {

    @Bean
    public RandomGenerator getRandomGenerator() {
        return new Random();
    }

    @Bean
    public Integer maxAmountLessons(@Value("${amount.maxLessons}") Integer maxLessons) {
        return maxLessons;
    }
}
