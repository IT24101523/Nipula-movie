package com.cineline.lanka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Define a simple password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Define in-memory users for testing
    @Bean
    public UserDetailsService userDetailsService() {
        // We will use one ADMIN account for all management tasks
        UserDetails admin = User.builder()
                .username("admin") // Easy username
                .password(passwordEncoder().encode("password")) // Easy password for development
                .roles("ADMIN")
                .build();

        // Optional: A regular user for later public site testing
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("userpass"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // Configure security rules
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to static assets (CSS, JS, Images, etc.)
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        // Admin URLs require the ADMIN role
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Allow public access to the main index and login page
                        .requestMatchers("/", "/login").permitAll()

                        // All other requests must be authenticated (logged in)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")               // Use a custom login page (which we need to create)
                        .defaultSuccessUrl("/admin/dashboard", true) // Always redirect Admin to dashboard after login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout") // Redirect to login page after successful logout
                        .permitAll()
                );

        return http.build();
    }
}