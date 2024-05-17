package ua.foxminded.WebProject.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.random.RandomGenerator;

@Component
@RequiredArgsConstructor
public class GroupNameGenerator {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWZYZ";
    private static final String NUMBERS = "0123456789";

    private final RandomGenerator randomGenerator;

    public String generateGroupName() {
        return String.format("%s%s-%s%s", randomChar(LETTERS), randomChar(LETTERS), randomChar(NUMBERS), randomChar(NUMBERS));
    }

    private char randomChar(String string) {
        return string.charAt(randomGenerator.nextInt(string.length()));
    }
}
