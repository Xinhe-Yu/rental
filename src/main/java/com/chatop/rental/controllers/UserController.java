package com.chatop.rental.controllers;

import com.chatop.rental.dto.UserDTO;
import com.chatop.rental.entities.User;
import com.chatop.rental.services.UserService;
import com.chatop.rental.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "User management APIs")
public class UserController {
  private final UserService userService;
  private final UserMapper mapper;

  public UserController(UserService userService, UserMapper mapper) {
    this.userService = userService;
    this.mapper = mapper;
  }

  @Operation(summary = "Get user by ID", description = "Retrieve detailed information about a specific user. Requires authentication.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
      @ApiResponse(responseCode = "401", description = "Not authenticated", content = @Content(schema = @Schema(type = "object", example = "{\"error\" : \"User not authenticated\""))),
      @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(type = "object", example = "{\"error\" : \"Rental not found\"")))
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok(mapper.convertToDTO(user));
  }
}
