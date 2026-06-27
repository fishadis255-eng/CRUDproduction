package com.example.springboot.cruddemo.Security;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import com.example.springboot.cruddemo.DAO.UserRepository;
import com.example.springboot.cruddemo.entity.UserEntity;

import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User; // Correct Import
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class DemoSecurityConfig {
    @Service
    public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository; // Your JPA repository for User entity

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            // Fetch your custom User entity from DB
            UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                
            // Map your entity to a Spring Security User
            return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // Should be BCrypt hashed
                .roles(user.getRole())
                .build();
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/register", "/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/employees").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/employees/**").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .requestMatchers("/api/auth/**").permitAll()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
