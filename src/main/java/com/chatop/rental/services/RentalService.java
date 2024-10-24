package com.chatop.rental.services;

import com.chatop.rental.configuration.CustomUserDetails;
import com.chatop.rental.dto.RentalDTO;
import com.chatop.rental.dto.RentalListDTO;
import com.chatop.rental.entities.Rental;
import com.chatop.rental.entities.User;
import com.chatop.rental.repositories.RentalRepository;
import com.chatop.rental.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
    }

    public RentalDTO getRentalById(Long id) {
        return rentalRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));
    }

    public void createRental(RentalDTO rentalDTO, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);

        Rental rental = new Rental();
        convertToEntity(rental, rentalDTO);
        rental.setOwner(user);
        rentalRepository.save(rental);
    }

    public void updateRental(Long id, RentalDTO rentalDTO, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

        if (!rental.getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to update this rental");
        }

        convertToEntity(rental, rentalDTO);
        rentalRepository.save(rental);
    }

    public void deleteRental(Long id, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));
        if (!rental.getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to update this rental");
        }

        rentalRepository.delete(rental);
    }

    private RentalDTO convertToDTO(Rental rental) {
        RentalDTO dto = new RentalDTO();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setPicturesFromString(rental.getPicture());
        dto.setDescription(rental.getDescription());
        dto.setOwnerId(rental.getOwnerId());
        dto.setCreatedAt(rental.getCreatedAt());
        dto.setUpdatedAt(rental.getUpdatedAt());
        return dto;
    }

    private Rental convertToEntity(Rental rental, RentalDTO dto) {
        rental.setName(dto.getName());
        rental.setSurface(dto.getSurface());
        rental.setPrice(dto.getPrice());
        rental.setPicture(dto.getPicturesAsString());
        rental.setDescription(dto.getDescription());
        return rental;
    }

    private RentalListDTO convertToListDTO(Rental rental) {
        RentalListDTO dto = new RentalListDTO();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setDescription(rental.getDescription());
        dto.setOwnerId(rental.getOwnerId());
        dto.setCreatedAt(rental.getCreatedAt());
        dto.setUpdatedAt(rental.getUpdatedAt());
        String[] pics = rental.getPicture().split(",");
        dto.setPicture(pics.length > 0 ? pics[0] : "");
        return dto;
    }

    private User getUserFromUserDetails(CustomUserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
