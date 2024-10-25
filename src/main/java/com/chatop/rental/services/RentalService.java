package com.chatop.rental.services;

import com.chatop.rental.configuration.CustomUserDetails;
import com.chatop.rental.dto.RentalDTO;
import com.chatop.rental.dto.RentalListDTO;
import com.chatop.rental.dto.RentalRequestDTO;
import com.chatop.rental.dto.RentalUpdateDTO;
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

    public void createRental(RentalRequestDTO rentalDTO, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        Rental rental = new Rental();
        convertToEntity(rental, rentalDTO);
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

            rental.setName(name);
            rental.setSurface(surface);
            rental.setPrice(price);
            rental.setDescription(description);
            rentalRepository.save(rental);

    }

    public void updateRentalwithInputStream(Long id, InputStream inputStream, CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

        if (!rental.getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to update this rental");
        }

        try {
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String[] values = extractValuesFromMultipart(body);
            String name = values[0];
            String surface = values[1];
            String price = values[2];
            String description = values[3];
            rental.setName(name);
            rental.setSurface(Double.parseDouble(surface));
            rental.setPrice(Double.parseDouble(price));
            rental.setDescription(description);
            rentalRepository.save(rental);
        } catch ( IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request");
        }
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

    private String[] extractValuesFromMultipart(String body) {
        String[] values = new String[4];
        String boundary = "--" + body.split("\n")[0].trim();
        String[] parts = body.split(boundary);

        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;
            if (part.contains("name=\"name\"")) {
                values[0] = extractValue(part);
            } else if (part.contains("name=\"surface\"")) {
                values[1] = extractValue(part);
            } else if (part.contains("name=\"price\"")) {
                values[2] = extractValue(part);
            } else if (part.contains("name=\"description\"")) {
                values[3] = extractValue(part);
            }
        }
        return values;
    }

    private String extractValue(String part) {
        // Find the start and end positions of the value
        String[] lines = part.split("\n");
        for (String line : lines) {
            if (line.startsWith("Content-Disposition:")) {
                continue;
            }
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        return "";
    }

    private RentalDTO convertToDTO(Rental rental) {
        RentalDTO dto = new RentalDTO();
        dto.setId(rental.getId());
        dto.setName(rental.getName());
        dto.setSurface(rental.getSurface());
        dto.setPrice(rental.getPrice());
        dto.setPicturesFromString(baseUrl, rental.getPicture());
        dto.setDescription(rental.getDescription());
        dto.setOwnerId(rental.getOwnerId());
        dto.setCreatedAt(rental.getCreatedAt());
        dto.setUpdatedAt(rental.getUpdatedAt());
        return dto;
    }

    private Rental convertToEntity(Rental rental, RentalRequestDTO dto) {
        String filePath = processPictures(dto.getPicture());

        rental.setName(dto.getName());
        rental.setSurface(dto.getSurface());
        rental.setPrice(dto.getPrice());
        rental.setPicture(filePath);
        rental.setDescription(dto.getDescription());
        return rental;
    }

    private String processPictures(MultipartFile picture) {
        if (picture == null || !fileService.isImageFile(picture)) {
            return "";
        }
        return fileService.saveMultipartFile(picture);
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
        dto.setUpdatedAt(rental.getUpdatedAt());;
        dto.setPicture(baseUrl, rental.getPicture());
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
