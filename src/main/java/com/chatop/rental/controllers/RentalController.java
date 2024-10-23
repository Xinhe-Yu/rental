package com.chatop.rental.controllers;

import com.chatop.rental.configuration.CustomUserDetails;
import com.chatop.rental.entities.Rental;
import com.chatop.rental.repositories.RentalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rentals")
public class RentalController {
    private final RentalRepository rentalRepository;

    public RentalController(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRentals(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(Map.of("error", "User not authenticated"), HttpStatus.UNAUTHORIZED);
        }

        List<Rental> rentals = rentalRepository.findAll();

        return ResponseEntity.ok(Map.of("rentals", rentals));
    }
}
