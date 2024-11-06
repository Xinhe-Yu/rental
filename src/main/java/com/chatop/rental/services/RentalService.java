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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Value("${app.upload.base-url}")
    private String baseUrl;

    @Value("${server.servlet.contextPath:/api}")
    private String contextPath;

    @Autowired
    public RentalService(RentalRepository rentalRepository,
                         UserRepository userRepository,
                         FileService fileService) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
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

    public void createRental(String name, Double surface, Double price, String description, MultipartFile picture, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        Rental rental = new Rental();
        convertCommonParamsToEntity(rental, name, surface, price, description);
        rental.setPicture(processPictures(picture));
        rental.setOwner(user);

        rentalRepository.save(rental);
    }

    public void updateRental(Long id, String name, Double surface, Double price, String description, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

        if (!rental.getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to update this rental");
        }
        convertCommonParamsToEntity(rental, name, surface, price, description);
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
        convertCommonParamsToDTO(rental, dto);
        dto.setPicturesFromString(baseUrl, rental.getPicture());
        return dto;
    }

    private RentalListDTO convertToListDTO(Rental rental) {
        RentalListDTO dto = new RentalListDTO();
        convertCommonParamsToDTO(rental, dto);
        dto.setPicture(baseUrl, rental.getPicture());
        return dto;
    }

    private RentalDTO convertCommonParamsToDTO(Rental rental, RentalDTO dto) {
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setDescription(rental.getDescription());
        dto.setOwnerId(rental.getOwnerId());
        dto.setCreatedAt(rental.getCreatedAt());
        dto.setUpdatedAt(rental.getUpdatedAt());
        return dto;
    }

    private Rental convertCommonParamsToEntity(Rental rental, String name, Double surface, Double price, String description) {
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        return rental;
    }

    private String processPictures(MultipartFile picture) {
        if (picture == null || !fileService.isImageFile(picture)) {
            return "";
        }
        return fileService.saveMultipartFile(picture);
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
