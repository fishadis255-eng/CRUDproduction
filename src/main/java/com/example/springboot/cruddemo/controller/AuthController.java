package com.example.springboot.cruddemo.controller;

import com.example.springboot.cruddemo.Security.JwtService;
import com.example.springboot.cruddemo.DAO.UserRepository;
import com.example.springboot.cruddemo.entity.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, 
                          JwtService jwtService, 
                          UserRepository userRepository, 
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserEntity user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user) {
        // Authenticate the user credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        // Fetch user from DB to generate token
        var userDetails = userRepository.findByUsername(user.getUsername())
                .orElseThrow();
        
        // Return JWT
        return jwtService.generateToken(org.springframework.security.core.userdetails.User
                .withUsername(userDetails.getUsername())
                .password(userDetails.getPassword())
                .roles(userDetails.getRole())
                .build());
    }
}