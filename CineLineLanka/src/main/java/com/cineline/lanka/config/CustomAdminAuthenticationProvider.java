package com.cineline.lanka.config;

// FIX: Use the path we confirmed from the image: com.cineline.lanka.model.security.Admin
import com.cineline.lanka.model.security.Admin;
// ASSUMED FIX: Use the new repository interface name: AdminRepository
import com.cineline.lanka.repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Optional;

@Component
public class CustomAdminAuthenticationProvider implements AuthenticationProvider {

    // NOTE: Changed from AdminUserRepository to AdminRepository
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAdminAuthenticationProvider(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 1. Look up the Admin in the database
        Optional<Admin> userOptional = adminRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        Admin adminUser = userOptional.get();

        // 2. Verify the password hash
        if (!passwordEncoder.matches(password, adminUser.getPassword())) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        // 3. Authentication successful. Prepare the authorities (roles).
        // Assuming your Admin entity has a getRole() method
        String role = adminUser.getRole();

        String authorityRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(authorityRole));

        // 4. Return a fully authenticated token
        return new UsernamePasswordAuthenticationToken(
                adminUser.getUsername(), // Principal
                null, // Credentials
                authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}