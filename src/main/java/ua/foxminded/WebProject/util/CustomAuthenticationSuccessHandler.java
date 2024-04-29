package ua.foxminded.WebProject.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String role = authentication.getAuthorities().toString();
        role = role.substring(6, role.length() - 1);

        switch (role) {
            case "admin" -> response.sendRedirect("/admin");
            case "teacher" -> response.sendRedirect("/teacher");
            case "student" -> response.sendRedirect("/student");
            case "staff" -> response.sendRedirect("/staff");
            default -> response.sendRedirect("/");
        }
    }
}
