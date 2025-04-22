package com.kartike.schoolvaccinationportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kartike.schoolvaccinationportal.model.User;
import com.kartike.schoolvaccinationportal.repository.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public String signUp(String username, String password, String emailId, String contactNo) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User(username, password, emailId, contactNo);
        userRepository.save(user);
        return "User registered successfully";
    }

    public String logIn(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        return "Authentication successful";
    }
}
