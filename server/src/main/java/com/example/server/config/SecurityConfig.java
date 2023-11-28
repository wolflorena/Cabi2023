package com.example.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    //required for hashing the passwords securely
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //this method is used for setting up the rules to secure the application.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authz -> authz
                        .antMatchers("/api/**").permitAll()  // Permit these paths without authentication
                        .anyRequest().authenticated())          // Other requests need to be authenticated
                .formLogin(form -> form
                        .loginPage("/login")                    // Custom login page, if you have one
                        .permitAll())
                .logout(logout -> logout.permitAll())
                .csrf().disable();     // Allow logout for everyone

        return http.build();
    }
}
