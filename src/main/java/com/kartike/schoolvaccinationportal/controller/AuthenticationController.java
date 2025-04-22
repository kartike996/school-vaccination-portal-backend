package com.kartike.schoolvaccinationportal.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kartike.schoolvaccinationportal.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> body) {
    	
        String username = body.get("username");
        String password = body.get("password");
        
        return ResponseEntity.ok(authenticationService.logIn(username, password));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Map<String, String> body) {
    	
        String username = body.get("username");
        String password = body.get("password");
        String emailId = body.get("emailId");
        String contactNo = body.get("contactNo");
        
        return ResponseEntity.ok(authenticationService.signUp(username, password ,emailId, contactNo));
    }
}
