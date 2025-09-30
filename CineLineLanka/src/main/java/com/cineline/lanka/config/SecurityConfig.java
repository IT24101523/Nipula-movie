package com.cineline.lanka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Define the PasswordEncoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Define the AuthenticationManager bean (Standard for Spring Boot 3+)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 3. Define the Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            // FIX: Inject the CustomAdminAuthenticationProvider as a method argument to resolve the circular dependency
            CustomAdminAuthenticationProvider customAdminAuthenticationProvider
    ) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Public access: CSS, JS, Images, Login, Registration
                        .requestMatchers(antMatcher("/css/**"),
                                antMatcher("/js/**"),
                                antMatcher("/images/**"),
                                antMatcher("/login"),
                                antMatcher("/registration")).permitAll()

                        // Role-based Access Control:
                        // Super Admin access only to Management
                        .requestMatchers(antMatcher("/admin/management/**")).hasRole("SUPER_ADMIN")

                        // General Admin Access to Dashboard and other /admin pages
                        // If rithmi (MARKETING_COORDINATOR) needs access to management, that role must be added here
                        .requestMatchers(antMatcher("/admin/**")).hasAnyRole("SUPER_ADMIN", "MANAGER", "FRONTDESK_EXECUTIVE", "MARKETING_COORDINATOR", "IT_SUPPORT_OFFICER")

                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(antMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                // *** NEW FIX: Custom Access Denied Page ***
                .exceptionHandling(exception -> exception
                        // Redirects to /403 URL (which must be handled by a controller method)
                        .accessDeniedPage("/403")
                )
                // 4. Use the injected custom authentication provider
                .authenticationProvider(customAdminAuthenticationProvider);

        return http.build();
    }
}