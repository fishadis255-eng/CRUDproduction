package com.example.springboot.cruddemo.Security;

import com.example.springboot.cruddemo.entity.UserEntity;
import com.example.springboot.cruddemo.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Fetch user from database
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2. Map database user to Spring Security's UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Must be BCrypt hashed
                .roles(user.getRole())        // Example: "EMPLOYEE", "MANAGER"
                .build();
    }
}
