package com.cineline.lanka.config;
// Note: Package path might be different based on your exact file location

import com.cineline.lanka.model.admin.Admin;
import com.cineline.lanka.repository.AdminRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class CustomAdminAuthenticationProvider implements AuthenticationProvider {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    // CONSTRUCTOR INJECTION: Correctly wires dependencies
    public CustomAdminAuthenticationProvider(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<Admin> adminOptional = adminRepository.findByUsername(username);

        if (adminOptional.isEmpty()) {
            throw new UsernameNotFoundException("Admin user not found: " + username);
        }

        Admin admin = adminOptional.get();

        if (!admin.isEnabled()) {
            throw new BadCredentialsException("User account is disabled.");
        }

        // Verify the provided password
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        // *** CRITICAL FIX FOR 403 ERROR ***
        // Ensure the role is prefixed with "ROLE_" to satisfy Spring Security's hasRole() check
        String roleWithPrefix = admin.getRole().startsWith("ROLE_") ? admin.getRole() : "ROLE_" + admin.getRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleWithPrefix);


        // Authentication successful
        return new UsernamePasswordAuthenticationToken(
                admin.getUsername(),
                admin.getPassword(),
                Collections.singletonList(authority)
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}