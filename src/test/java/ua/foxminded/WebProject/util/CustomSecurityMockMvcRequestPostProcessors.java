package ua.foxminded.WebProject.util;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

public class CustomSecurityMockMvcRequestPostProcessors {

    public static RequestPostProcessor testUser(String role) {
        return user("user@gmail.com").password("password").roles(role);
    }
}
