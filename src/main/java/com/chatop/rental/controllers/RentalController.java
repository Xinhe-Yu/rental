package com.chatop.rental.controllers;

import com.chatop.rental.configuration.CustomUserDetails;
import com.chatop.rental.dto.RentalDTO;
import com.chatop.rental.dto.RentalListDTO;
import com.chatop.rental.services.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rentals")
@Tag(name = "Rentals", description = "Rental management APIs")
public class RentalController {

    private final RentalService rentalService;
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Operation(
            summary = "Get a list of rentals.",
            description = "Retrieve all rentals with only one picture. Requires authentication."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of rentals retrieved with success",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RentalListDTO.class)))
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRentals(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(Map.of("error", "User not authenticated"), HttpStatus.UNAUTHORIZED);
        }

        List<RentalDTO> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(Map.of("rentals", rentals));
    }

    @Operation(
            summary = "Get rental by ID",
            description = "Retrieve detailed information about a specific rental including all pictures. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental found",
                    content = @Content(schema = @Schema(implementation = RentalDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(schema = @Schema(
                            type= "object",
                            example = "{\"error\" : \"User not authenticated\""
                    ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Rental not found",
                    content = @Content(schema = @Schema(
                            type= "object",
                            example = "{\"error\" : \"Rental not found\""
                    ))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Long id) {
        RentalDTO rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @Operation(
            summary = "Create a new rental",
            description = "Create a new rental. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental created with success",
                    content = @Content(schema = @Schema(
                            type = "object",
                            example = "{\"message\": \"Rental created !\"}"
                    ))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(
                            type= "object",
                            example = "{\"error\" : \"Invalid input\""
                    ))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated",
                    content = @Content(schema = @Schema(
                            type= "object",
                            example = "{\"error\" : \"User not authenticated\""
                    ))
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        rentalService.createRental(name, surface, price, description, picture, userDetails);
        return ResponseEntity.ok(Map.of("message", "Rental created"));
    }

    @Operation(
            summary = "Update a rental",
            description = "Update a rental. Only the owner of the rental can modify it."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental updated with success",
                    content = @Content(schema = @Schema(
                            type = "object",
                            example = "{\"message\": \"Rental updated !\"}"
                    ))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(
                            type= "object",
                            example = "{\"error\" : \"Invalid input\""
                    ))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User not authenticated",
                    content = @Content(schema = @Schema(
                            type= "object",
                            example = "{\"error\" : \"User not authenticated\""
                    ))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to update this rental",
                    content = @Content(schema = @Schema(
                            type= "object",
                            example = "{\"error\" : \"Not authorized to update this rental\""
                    ))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Rental not found",
                    content = @Content(schema = @Schema(
                            type= "object",
                            example = "{\"error\" : \"Rental not found\""
                    ))
            )
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> updateRental(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        rentalService.updateRental(id, name, surface, price, description, userDetails);
        return ResponseEntity.ok(Map.of("message", "Rental updated !"));
    }
}
