package com.chatop.rental.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.chatop.rental.dto.RentalDTO;
import com.chatop.rental.dto.RentalListDTO;
import com.chatop.rental.entities.Rental;

@Component
public class RentalMapper {
  @Value("${app.upload.base-url}")
  private String baseUrl;

  @Value("${server.servlet.contextPath:/api}")
  private String contextPath;

  public RentalDTO convertToDTO(Rental rental) {
    RentalDTO dto = new RentalDTO();
    convertCommonParamsToDTO(rental, dto);
    dto.setPicturesFromString(baseUrl, rental.getPicture());
    return dto;
  }

  public RentalListDTO convertToListDTO(Rental rental) {
    RentalListDTO dto = new RentalListDTO();
    convertCommonParamsToDTO(rental, dto);
    dto.setPicture(baseUrl, rental.getPicture());
    return dto;
  }

  public RentalDTO convertCommonParamsToDTO(Rental rental, RentalDTO dto) {
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

  public List<RentalDTO> convertToListDTOs(List<Rental> rentals) {
    return rentals.stream()
        .map(this::convertToListDTO)
        .collect(Collectors.toList());
  }
}
