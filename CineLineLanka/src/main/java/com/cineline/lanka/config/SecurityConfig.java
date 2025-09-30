package com.cineline.lanka.config;

// NOTE: We don't need to import this anymore, but it won't hurt if you leave it.
// import com.cineline.lanka.config.CustomAdminAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired; // KEEP this if you need it for other beans
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. REMOVE the @Autowired injection of the provider
    // @Autowired
    // private CustomAdminAuthenticationProvider customAdminAuthenticationProvider; // <-- DELETE OR COMMENT OUT

    // Must be defined for password hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configure the Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REMOVE the explicit .authenticationProvider() call!
                // Spring will find the @Component-annotated provider automatically.
                // .authenticationProvider(customAdminAuthenticationProvider) // <-- DELETE OR COMMENT OUT

                .authorizeHttpRequests(authorize -> authorize
                        // Allow public access to the root, login, and static resources
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()

                        // Restrict access based on roles from the AdminUser table:

                        // Super Admin Restricted Paths
                        .requestMatchers("/admin/users/**", "/admin/admins/**")
                        .hasRole("SUPER_ADMIN")

                        // Manager/Super Admin Restricted Paths
                        .requestMatchers("/admin/movies/**")
                        .hasAnyRole("SUPER_ADMIN", "MANAGER")

                        // General Staff (All Admin Roles)
                        .requestMatchers("/admin/**")
                        .hasAnyRole("SUPER_ADMIN", "MANAGER", "FRONTDESK_EXECUTIVE")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}