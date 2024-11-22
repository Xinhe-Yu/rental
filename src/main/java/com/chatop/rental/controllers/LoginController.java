package com.chatop.rental.controllers;

import com.chatop.rental.dto.LoginRequestDTO;
import com.chatop.rental.dto.UserDTO;
import com.chatop.rental.dto.UserRegisterDTO;
import com.chatop.rental.services.CustomUserDetailsService;
import com.chatop.rental.services.JWTService;
import com.chatop.rental.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;
import com.chatop.rental.entities.User;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class LoginController {

  private final CustomUserDetailsService userDetailsService;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JWTService jwtService;

  public LoginController(CustomUserDetailsService userDetailsService,
      UserService userService,
      AuthenticationManager authenticationManager,
      JWTService jwtService) {
    this.userDetailsService = userDetailsService;
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @Operation(summary = "Create a new count", description = "Create a new count.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Count created with success", content = @Content(schema = @Schema(type = "object", example = "{\"token\": \"eyJHbGciOiJIUzI1N... !\"}"))),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(type = "object", example = "{\"error\": \"Registration failed !\"}"))),

  })
  @PostMapping("/register")
  public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserRegisterDTO userDTO) {
    try {
      userDetailsService.save(userDTO);
      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
      String token = jwtService.generateToken(authentication);
      return ResponseEntity.ok(Map.of("token", token));
    } catch (Exception e) {
      return new ResponseEntity<>(Map.of("error", "Registration failed: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }
  }

  @Operation(summary = "Login user", description = "Authenticate user with email and password and return JWT token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(type = "object", example = "{\"token\": \"eyJHbGciOiJIUzI1N... !\"}"))),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(schema = @Schema(type = "object", example = "{\"error\": \"Authentication failed\"}")))
  })
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO dto) {
    try {
      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
      String token = jwtService.generateToken(authentication);
      return ResponseEntity.ok(Map.of("token", token));
    } catch (AuthenticationException e) {
      return new ResponseEntity<>(Map.of("error", "Authentication failed"), HttpStatus.UNAUTHORIZED);
    }
  }

  @Operation(summary = "Get current user information", description = "Retrieve information about the currently authenticated user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Current user information send with success", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
      @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content(schema = @Schema(type = "object", example = "{\"error\": \"User not authenticated\"}")))
  })
  @GetMapping("/me")
  public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
    User user = userDetailsService.getCurrentUser(userDetails.getUsername());
    UserDTO dto = userService.convertToDTO(user);
    return ResponseEntity.ok(dto);
  }
}
