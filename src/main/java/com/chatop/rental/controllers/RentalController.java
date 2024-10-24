package com.chatop.rental.controllers;

import com.chatop.rental.configuration.CustomUserDetails;
import com.chatop.rental.dto.RentalDTO;
import com.chatop.rental.entities.Rental;
import com.chatop.rental.repositories.RentalRepository;
import com.chatop.rental.services.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    private final RentalService rentalService;
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRentals(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(Map.of("error", "User not authenticated"), HttpStatus.UNAUTHORIZED);
        }

        List<RentalDTO> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(Map.of("rentals", rentals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Long id) {
        RentalDTO rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createRental(@RequestBody RentalDTO rentalDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        rentalService.createRental(rentalDTO, userDetails);
        return ResponseEntity.ok(Map.of("message", "Rental created"));
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> updateRental(@PathVariable Long id, @RequestBody RentalDTO rentalDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        rentalService.updateRental(id, rentalDTO, userDetails);
        return ResponseEntity.ok(Map.of("message", "Rental updated !"));
    }


}
