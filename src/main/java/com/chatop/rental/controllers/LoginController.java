package com.chatop.rental.controllers;

import com.chatop.rental.dto.LoginRequest;
import com.chatop.rental.dto.UserDTO;
import com.chatop.rental.services.CustomUserDetailsService;
import com.chatop.rental.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.HashMap;
import java.util.Map;
import com.chatop.rental.entities.User;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public LoginController(CustomUserDetailsService userDetailsService,
                           AuthenticationManager authenticationManager,
                           JWTService jwtService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userDetailsService.save(userDTO);
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Registration failed: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            String token = jwtService.generateToken(authentication);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(Map.of("error", "Authentication failed"), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/gogo")
    public String getResource() {
        return "a value...";
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(Map.of("error", "User not authenticated"), HttpStatus.UNAUTHORIZED);
        }
        try {
            User user = userDetailsService.getCurrentUser(userDetails.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("created_at", user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");
            response.put("updated_at", user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : "");
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.NOT_FOUND);
        }

    }
//    @GetMapping("/user")
//    public String getUser() {
//        return "Welcome, User";
//    }
//
//    @GetMapping("/admin")
//    public String getAdmin() {
//        return "Welcome, Admin";
//    }
}
