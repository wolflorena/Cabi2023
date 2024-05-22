package com.example.server.controller;

import com.example.server.repository.DTOs.Authentication.AuthenticationRequestDTO;
import com.example.server.repository.DTOs.Authentication.AuthenticationResponseDTO;
import com.example.server.repository.entity.CustomUserDetails;
import com.example.server.repository.entity.Customer;
import com.example.server.repository.entity.Doctor;
import com.example.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDTO authRequest) throws AuthenticationException {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            // If the above check doesn't return an authenticated user, an exception will be thrown.
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Creating a set of strings from GrantedAuthorities for if checks
            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            Long user_id = userDetails.getId();
            String role = "ROLE_ADMIN";  // Default to admin if no other roles are matched
            if (roles.contains("ROLE_CUSTOMER")) {
                role = "ROLE_CUSTOMER";
            } else if (roles.contains("ROLE_DOCTOR")) {
                role = "ROLE_DOCTOR";
            }

            final String jwt = jwtUtil.generateToken(authRequest.getEmail(), role, user_id);
            return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }
}