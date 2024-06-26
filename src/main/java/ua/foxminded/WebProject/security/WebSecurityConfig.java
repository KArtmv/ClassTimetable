package ua.foxminded.WebProject.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/css/**", "/webjars/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login*").anonymous()
                        .requestMatchers("/admin").hasRole("admin")
                        .requestMatchers("/student").hasAnyRole("student", "admin", "staff")
                        .requestMatchers("/teacher").hasAnyRole("teacher", "admin", "staff")
                        .requestMatchers("/course", "/group", "/lesson", "/staff").hasAnyRole("admin", "staff")
                        .anyRequest().authenticated())
                .formLogin(login -> login.loginPage("/login")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler("/login-error")))
                .httpBasic(withDefaults())
                .logout(logout -> logout.permitAll().logoutSuccessUrl("/"));

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
