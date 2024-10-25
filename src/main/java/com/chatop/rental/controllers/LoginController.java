package com.chatop.rental.controllers;

import com.chatop.rental.dto.LoginRequest;
import com.chatop.rental.dto.RentalDTO;
import com.chatop.rental.dto.UserDTO;
import com.chatop.rental.dto.UserRegisterDTO;
import com.chatop.rental.services.CustomUserDetailsService;
import com.chatop.rental.services.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Authentication management APIs")
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

    @Operation(
            summary = "Create a new count",
            description = "Create a new count."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental created with success",
                    content = @Content(schema = @Schema(
                            type = "object",
                            example = "{\"message\": \"User registered successfully !\"}"
                    ))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(schema = @Schema(
                            type = "object",
                            example = "{\"error\": \"Registration failed !\"}"
                    ))
            ),

    })
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserRegisterDTO userDTO) {
        try {
            userDetailsService.save(userDTO);
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Registration failed: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Login user",
            description = "Authenticate user with email and password and return JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(schema = @Schema(
                            type = "object",
                            example = "{\"token\": \"eyJHbGciOiJIUzI1N... !\"}"
                    ))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication failed",
                    content = @Content(schema = @Schema(
                            type = "object",
                            example = "{\"error\": \"Authentication failed\"}"
                    ))
            )
    })
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

    @Operation(
            summary = "Get current user information",
            description = "Retrieve information about the currently authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authenticated",
                    content = @Content(schema = @Schema(
                            type = "object",
                            example = "{\"error\": \"User not authenticated\"}"
                    ))
            )
    })
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
}
